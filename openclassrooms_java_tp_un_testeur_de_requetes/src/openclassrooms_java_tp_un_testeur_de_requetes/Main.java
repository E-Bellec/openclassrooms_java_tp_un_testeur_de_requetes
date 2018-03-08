package openclassrooms_java_tp_un_testeur_de_requetes;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import openclassrooms_java_tp_un_testeur_de_requetes.view.MappingContentPane;
import openclassrooms_java_tp_un_testeur_de_requetes.view.MappingSqlSearcher;


public class Main extends Application {

	private Stage primaryStage; // Conteneur principale (Stage)
	private Stage stageDialogue;
	private BorderPane contentPane;// backgroud de l'interface graphique qui va contenir toue les éléments graphiques
	
	// CONSTRUCTEUR
	public Main() {
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Requetteur Sql");
		
		//Nous allons utiliser nos fichier FXML dans ces deux méthodes
		initializationOfTheMainContainer();
		initializationContent();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	// METHODE initializationOfTheMainContainer
	private void initializationOfTheMainContainer() {
		
		//On créé un chargeur de FXML
		FXMLLoader loader = new FXMLLoader();
		
		//On lui spécifie le chemin relatif à notre classe du fichier FXML a charger : dans le sous-dossier view
		loader.setLocation(Main.class.getResource("view/FxmlContentPane.fxml"));
		try {
			
			//Le chargement nous donne notre conteneur
			contentPane = (BorderPane) loader.load();
			
			//On définit une scène principale avec notre conteneur
			Scene scene = new Scene(contentPane);
			primaryStage.setScene(scene);// Que nous affectons à notre Stage
			primaryStage.show();// Pour l'afficher
			
			// Initialisation de notre contrôleur
	        MappingContentPane contentPaneMapping = loader.getController();
	        // On spécifie la classe principale afin de pour récupérer le Stage et ainsi fermer l'application
	        contentPaneMapping.setMainApp(this);
		    
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // FIN METHODE initializationOfTheMainContainer
	
	// METHODE initializationContent
	private void initializationContent() {
		
		//On créé un chargeur de FXML
		FXMLLoader loader = new FXMLLoader();
		
		//On lui spécifie le chemin relatif à notre classe du fichier FXML a charger : dans le sous-dossier view
		loader.setLocation(Main.class.getResource("view/FxmlSqlSearcher.fxml"));
		try {
			//Nous récupérons notre conteneur qui contiendra les données
			AnchorPane contentApp = (AnchorPane) loader.load();
			contentPane.setCenter(contentApp);// nous ajoutons à notre conteneur principal au centre, puisque'il s'agit d'un BorderPane
			MappingSqlSearcher controleur = loader.getController();// Nous récupérons notre mappeur via l'objet FXMLLoader
			
			//Nous lui passons notre instance de classe pour qu'il puisse récupérer notre liste observable
			controleur.setMainApp(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	} // FIN METHODE initializationContent
		
	// GETTER
		// Accesseur du contenneur principale (application)
		public Stage getPrimaryStage() {
			return primaryStage;
		}
		
		// Accesseur du contenneur de la fenêtre de dialogue (edition personnage)
		public Stage getStageDialogue() {
			return stageDialogue;
		}
		
}
