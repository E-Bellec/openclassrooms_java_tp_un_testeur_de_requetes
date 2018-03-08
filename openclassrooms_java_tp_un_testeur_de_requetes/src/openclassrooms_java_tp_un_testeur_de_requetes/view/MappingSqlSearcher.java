package openclassrooms_java_tp_un_testeur_de_requetes.view;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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
		String query = queryValue.getText();
		
		try {
			long durationExecutionRequest = System.currentTimeMillis();
			
			// On se connecte à la base
			Statement state = SdzConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// On execute la requete
			ResultSet result = state.executeQuery(query);
			ResultSetMetaData meta = result.getMetaData(); // On récupère les meta pour avoir le nom des collonnes
			
			// On appel la méthode qui va lire le resultat de la requete et nous crée un tableau
			readQueryResult( result, meta );
			
			//On ferme le tout                                     
			result.close();
			state.close();
			
			durationExecutionRequest = System.currentTimeMillis() - durationExecutionRequest;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Méthode qui se charge de la lecture du résultat de la requete
	private void readQueryResult( ResultSet resultSet, ResultSetMetaData resultSetMetaData) {
		try {
			Integer columnNumber = resultSetMetaData.getColumnCount();
			Integer j = 0; // variable à incrémenté pour simuler chaque ligne du résultat de la requête
			String[] columnName = null;
			Object[][] lineValue = null;
			
			
			
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
				
			} else {
				
			} // FIN else
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Setter permet de récupérer tout le contenu et avoir la main sur l'application, et récupérer la liste des objet observable
	public void setMainApp(Main mainApp) {
		this.main = mainApp;
		
	}
		
	
	
}
