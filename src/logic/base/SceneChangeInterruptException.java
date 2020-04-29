package logic.base;

import application.GameSceneManager;

public class SceneChangeInterruptException extends GameInterruptException {
	private GameSceneManager scene;

	public SceneChangeInterruptException(GameSceneManager scene) {
		this.scene = scene;
	}

	public GameSceneManager getScene() {
		return scene;
	}

}
