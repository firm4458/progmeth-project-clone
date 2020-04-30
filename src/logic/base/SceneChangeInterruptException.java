package logic.base;

import application.GameScene;

public class SceneChangeInterruptException extends GameInterruptException {
	private GameScene scene;

	public SceneChangeInterruptException(GameScene scene) {
		this.scene = scene;
	}

	public GameScene getScene() {
		return scene;
	}

}
