package application;

import drawing.Renderer;
import javafx.animation.AnimationTimer;

public class GameManager {
	private GameSceneManager currentScene;
	private static GameManager gameManager;
	private AnimationTimer timer;
	public static void init(GameSceneManager initialScene) {
		gameManager = new GameManager();
		gameManager.currentScene = initialScene;
		gameManager.timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				gameManager.getCurrentScene().update();
				Renderer.getInstance().render();
			}
		};
		gameManager.timer.start();
	}
	public void setScene(GameSceneManager scene) {
		currentScene.destroy();
		currentScene = scene;
	}
	public static GameManager getInstance() {
		return gameManager;
	}
	public GameSceneManager getCurrentScene() {
		return currentScene;
	}
	
}
