package openclassrooms_java_jdbc_connexion_simplifiee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import openclassrooms_java_tp_un_testeur_de_requetes.ignore.parameter.ParameterSql;

public class SdzConnection {
	private static String url = ParameterSql.getUrl();
	private static String user = ParameterSql.getUser();
	private static String passwd = ParameterSql.getPasswd();
	private static Connection connect;
	 
	public static Connection getInstance(){
		if(connect == null){
			try {
				connect = DriverManager.getConnection(url, user, passwd);
			} catch (SQLException e) {
				e.printStackTrace();
				
				Alert bye = new Alert(AlertType.INFORMATION);
				bye.setTitle("ERREUR DE CONNEXION !");
				bye.setHeaderText("Il semble qu'il y est un problème de connexion avec la base de données !");
				bye.setContentText("Message : " + e.getMessage());
				bye.showAndWait(); // On affiche et on attend la reponse utilisateur
			}
		}
		return connect;
	}
}
