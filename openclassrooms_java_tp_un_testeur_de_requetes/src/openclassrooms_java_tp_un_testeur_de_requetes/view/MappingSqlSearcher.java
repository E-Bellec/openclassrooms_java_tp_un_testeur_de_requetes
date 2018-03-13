package openclassrooms_java_tp_un_testeur_de_requetes.view;

import java.awt.Dialog;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JTextArea;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import openclassrooms_java_jdbc_connexion_simplifiee.SdzConnection;

public class MappingSqlSearcher {
	
	@FXML // Contenneur du resultat de la requête.
	private BorderPane borderPaneQueryResult;
	@FXML // varriable qui contiendra la valeur du champs de la requete
	private TextArea queryValue;
	//private double tableViewHeigth = borderPaneQueryResult.getHeight();
	//private double tableViewWidth = borderPaneQueryResult.getWidth();
	
	private String defaultQuery = 
			"SELECT * FROM public.classe;"; // Text par defaut
	private JTextArea querySqlFields = new JTextArea(defaultQuery); // Récupération du textArea contenant la requête de l'utilisateur
	
	@FXML //Méthode qui initialise notre interface graphique avec nos données métier
	private void initialize() {
		
		// On passe la valeur par defaut à notre interface graphique
		queryValue.setText(querySqlFields.getText());
		
		//Nous récupérons le model de notre tableau (vous connaissez maintenant)
	    //où nous récupérons l'item sélectionné et où nous y attachons un écouteur qui va utiliser notre méthode de mise à jour d'IHM
		
	}
	
	@FXML // Permet de lancer le processus, verifier la requete puis la lancer
	public void runQuery() {
		
		// On vide le contenu On vide là contenue de la précédente requête nue de la précédente requête
		borderPaneQueryResult.getChildren().clear();
		
		long durationExecutionRequest;
		int numberLignesResult = 0;
		Statement state = null;
		ResultSet result = null;
		ResultSetMetaData meta;
		String query = queryValue.getText();
		Map<String[], Object[][]> resultQuery = null;
		String[] collumnName = null;
		Object[][] lineValue = null;
		TableView<ObservableList<String>> tableView = new TableView<ObservableList<String>>();
		TableColumn<ObservableList<String>, String> tableColumn;
		ObservableList<ObservableList<String>> data= FXCollections.observableArrayList();
		
		try {
			// On initialise le timer PUIS on instancie la connexion avec la base de données
			durationExecutionRequest = System.currentTimeMillis();
			state = SdzConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			result = state.executeQuery(query);// // On execute la requete
			meta = result.getMetaData(); // On récupère les meta pour avoir le nom des collonnes
			durationExecutionRequest = System.currentTimeMillis() - durationExecutionRequest; // On récupère le temps d'execution
			
			// On appel la méthode qui va lire le resultat de la requete et nous crée un tableau
			resultQuery = readQueryResult( result, meta ); 
			
			// Parcour de l'objet map
			Set<Entry<String[], Object[][]>> setHm = resultQuery.entrySet();
			Iterator<Entry<String[], Object[][]>> it = setHm.iterator();
			
			// On parcourt l'objet map, ici on à une seule itération
			while(it.hasNext()){
				
				Entry<String[], Object[][]> e = it.next();
				//System.out.println(e.getKey() + " : " + e.getValue());
				
				collumnName = e.getKey();
				lineValue = e.getValue();
				
			} // FIN while
				
			// On boucle sur le nombre de nom de colonne
			for ( int i = 0; i < collumnName.length; i++ ) {
				
				final int finalIdx = i;
				// On crée une colonnes avec le nom
				tableColumn = new TableColumn<>( collumnName[i] );
				tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(finalIdx))); // On ajout les valeurs
				tableColumn.setPrefWidth(borderPaneQueryResult.getWidth()/collumnName.length); // On définit la taille de chaque colonne
				
				// On ajout le contenue dans le tableau
				tableView.getColumns().add(tableColumn);
				
			} // FIN for collumnName
				
			// On boucle sur le nombre de ligne de résultat
			for (int o = 0; o < lineValue.length; o++) {
				// Iterate Row
	            ObservableList<String> row = FXCollections.observableArrayList();
	            numberLignesResult = 0;
	            
	            // On boucle sur le nb de résultat par lignes
				for (int p = 0; p < lineValue[o].length; p++) {
					numberLignesResult = p;
					row.add(lineValue[o][p].toString());
				}
				
                data.add(row);
			} // FIN for lineValue
			
			// On ajoute le donnée dans la tables
			tableView.setItems(data);
			tableView.setPrefHeight(borderPaneQueryResult.getHeight()); // On definit la hauteur du tableau
			tableView.setPrefWidth(borderPaneQueryResult.getWidth()); // On definit la taille
			borderPaneQueryResult.setCenter(tableView); // on insert le tout dans le composant graphique borderPane
			borderPaneQueryResult.setBottom(
					new HBox(new Label("La requête à été exécuter en " + durationExecutionRequest + " ms et a retourné " + numberLignesResult + " ligne(s)"))
			);

			// On ferme les ressources
			state.close();
			result.close();
		} catch (SQLException e) {
			
			// On affiche une erreur
			Alert bye = new Alert(AlertType.INFORMATION);
			bye.setTitle("Erreur !");
			bye.setHeaderText("Il semble qu'il y est un problème avec la requête sql !");
			bye.setContentText("Message : " + e.getMessage());
			bye.showAndWait(); // On affiche et on attend la reponse utilisateur
			
		} finally { // Fermetture des objets même lors d'un catch
			
			try {
				// On ferme les ressources
				if (state != null) { state.close(); }
				if (result != null)  { result.close(); }
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} // FIN finally
		
	} // FIN METHODE FXLM runQuery
	
	// Méthode qui se charge de la lecture du résultat de la requete
	private Map<String[], Object[][]> readQueryResult( ResultSet resultSet, ResultSetMetaData resultSetMetaData) {
		Integer columnNumber = null;
		Integer j = 0; // variable à incrémenté pour simuler chaque ligne du résultat de la requête
		String[] columnName = null;
		Object[][] lineValue = null;
		Map<String[], Object[][]> objectReturn = new HashMap<>();
		
		try {
			// On récupère le nombre de colonne des metas
			columnNumber = resultSetMetaData.getColumnCount();
			
			// Si les metas contiennent un résultat
			if ( columnNumber != null && columnNumber != 0 ) {
				
				// On initialise le tableau avec le bon nombre de colonnes
				columnName = new String[columnNumber];
				
				// On bloucle sur le nombres de collonnes
				for ( int i = 1; i-1 < columnNumber; i++) {
					
					// On récupère le nom de la collone parcourue
					columnName[i-1] = resultSetMetaData.getColumnName(i);
					
				} // FIN for
				
				// On initialise l'objet qui va contenir la ligne parcourue et la valeur de la collonne
				resultSet.last(); // On va à la dernière ligne de resultat de la requête
				lineValue = new Object[resultSet.getRow()][resultSetMetaData.getColumnCount()]; // initialisation de l'objet
				resultSet.beforeFirst(); // On revient au depart
				
				// On boucle tant qu'il reste des lignes
				while(resultSet.next()){ // ResultSet est initialement positionné avant la première ligne
					
					// On bloucle sur le nombres de collonnes
					for(int o = 1; o <= resultSetMetaData.getColumnCount(); o++) {
						
						// On récupère les valeurs de la ligne de résutat parcoure
						lineValue[j][o-1] = resultSet.getObject(o);
						
					} // FIN for
						
					j++; // On incrémente j pour la prochaine ligne
				} // FIN while
				
				// On remplit la varriable de retour
				objectReturn.put(columnName, lineValue);
				
			} else {
				System.out.println("PAS DE RESULTAT");
			} // FIN else
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return objectReturn;
		
	} // FIN METHODE readQueryResult
	
}
