package openclassrooms_java_tp_un_testeur_de_requetes.view;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import openclassrooms_java_jdbc_connexion_simplifiee.SdzConnection;
import openclassrooms_java_tp_un_testeur_de_requetes.Main;

public class MappingSqlSearcher {
	
	@FXML
	private TextArea queryValue; // varriable qui contiendra la valeur du champs de la requete
	
	private Main main;// Varriable qui contient le main, pour avoir la liste des objet observable
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
		long durationExecutionRequest = System.currentTimeMillis();
		Statement state = null;
		ResultSet result = null;
		ResultSetMetaData meta;
		String query = queryValue.getText();
		Map<String[], Object[][]> resultQuery = null;
		String[] collumnName = null;
		Object[][] lineValue = null;
		
		try {
			
			// On se connecte à la base
			state = SdzConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// On execute la requete
			result = state.executeQuery(query);
			meta = result.getMetaData(); // On récupère les meta pour avoir le nom des collonnes
			
			// On appel la méthode qui va lire le resultat de la requete et nous crée un tableau
			resultQuery = readQueryResult( result, meta );
			
			// Parcour de l'objet map
			Set<Entry<String[], Object[][]>> setHm = resultQuery.entrySet();
			Iterator<Entry<String[], Object[][]>> it = setHm.iterator();
			
			while(it.hasNext()){
				Entry<String[], Object[][]> e = it.next();
				//System.out.println(e.getKey() + " : " + e.getValue());
				
				collumnName = e.getKey();
				lineValue = e.getValue();
				
				System.out.println("Number Name : " + collumnName.length);
				System.out.println("Number objet line : " + lineValue.length);
				
				
				// On boucle sur le nombre de nom de colonne
				for ( int i = 0; i < collumnName.length; i++ ) {
					
					System.out.print("name = " +collumnName[i] + " && ");
					
				} // FIN for collumnName
				
				System.out.println("\n-----------------------------------------------------");
				
				// On boucle sur le nombre de ligne de résultat
				for (int o = 0; o < lineValue.length; o++) {
					
					System.out.println( "Ligne : " + o);
					
					for ( int p = 0; p < lineValue[o].length; p++ ) {
						System.out.println( "Nombre de resultat par lignes : " + lineValue[o][p]);
					}
					System.out.println( "FIN Ligne : " + o);
					
				} // FIN for lineValue
				
			} // FIN while
			
			//On ferme le tout                                     
			result.close();
			state.close();
			
			durationExecutionRequest = System.currentTimeMillis() - durationExecutionRequest;
			
			// On ferme les ressources
			state.close();
			result.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	// Setter permet de récupérer tout le contenu et avoir la main sur l'application, et récupérer la liste des objet observable
	public void setMainApp(Main mainApp) {
		this.main = mainApp;
		
	}
		
	
	
}
