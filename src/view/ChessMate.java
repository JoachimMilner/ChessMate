package view;

import java.io.IOException;

import controller.GUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Author Joachim Milner 2016
 */

public class ChessMate extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI.fxml"));
		primaryStage.setScene(new Scene(fxmlLoader.load()));
		GUIController controller = fxmlLoader.<GUIController>getController();
		controller.setPrimaryStage(primaryStage);
		setUserAgentStylesheet(STYLESHEET_CASPIAN);
		primaryStage.setTitle("ChessMate 9000");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();
		}

	public static void main(String[] args) {
		launch(args);
	}

}
