package openclassrooms_java_tp_un_testeur_de_requetes.view;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import openclassrooms_java_tp_un_testeur_de_requetes.Main;

public class MappingContentPane {

	private Main main; // Varriable qui contient le main pour pouvoir mettre a jour la fenètre et la fermer
	
	// Setter permettant de mettre à jour le main dans la classe actuel, permet de fermer l'application
	public void setMainApp(Main mainApp) {
		this.main = mainApp;
	}
	
	
	@FXML //Fermer l'application
	public void closeApp() {
		//On affiche un message car on est poli.
		Alert bye = new Alert(AlertType.INFORMATION);
		bye.setTitle("Au revoir !");
		bye.setHeaderText("See you soon...");
		bye.setContentText("Et merci d'avoir suivi ce cours");
		bye.showAndWait(); // On affiche et on attend la reponse utilisateur
		
		//Et on clos le stage principal, donc l'application
		this.main.getPrimaryStage().close();
	}
	
	
	@FXML // Remise à zero du champs de la requête
	public void newApp() {
		
		System.out.println("New apppp !");
		
		//On affiche la popup avec une personne inexistante
		// this.main.affichePersonneDialogue(new People(), "Création d'une personne");       
	}
	
}
