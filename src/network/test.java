package network;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class test extends Application {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		ChatInterface chat = new ChatInterface(1, 1);
		Scene sc = new Scene(chat.getPane());
		primaryStage.setScene(sc);
		primaryStage.show();
	}
}
