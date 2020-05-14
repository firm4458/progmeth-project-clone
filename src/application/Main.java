package application;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.util.ResourceManager;

public class Main extends Application {
	public static Stage stage;

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		primaryStage.setTitle("Space Master");
		primaryStage.getIcons().add(ResourceManager.getImage("lavaPlanet"));
		primaryStage.setResizable(false);
		GameManager.getInstance().init(new MenuScene("menu"));
	}

	public static void main(String[] args) {
		launch(args);
	}
}
