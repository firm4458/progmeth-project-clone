package logic;

import java.util.ArrayList;

import application.GameManager;
import application.GameScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.ConstantSpeedMove;



public class LoopBackground extends GameObject {

	public static void createLoopBackground(GameScene scene, Image img, double speed, int total,
			ArrayList<ScriptFactory> factories) {
		for (int i = 0; i < total; ++i) {
			GameObject background = new LoopBackground(img, speed, i, total);
			if (factories != null) {
				factories.forEach((factory) -> {
					background.addScript(factory.createScript());
				});
			}
			scene.addGameObject(background);
		}
	}

	private LoopBackground(Image img, double speed, int n, int total) {
		super(-5, 0);
		ImageSprite imgSprite = new ImageSprite(this, img);
		setSprite(imgSprite);
		double scale = (GameManager.NATIVE_WIDTH + 10) / img.getWidth();
		imgSprite.setScale(scale);
		imgSprite.setZ(-99);
		setY(GameManager.NATIVE_HEIGHT - (n + 1) * img.getHeight() * scale);

		addScript(new ConstantSpeedMove(0, speed));

		double loopBackY = GameManager.NATIVE_HEIGHT - total * img.getHeight() * scale;
		addScript(new Script() {
			GameObject parent;

			@Override
			public void update() {
				if (parent.getY() >= GameManager.NATIVE_HEIGHT) {
					double offset = parent.getY() - GameManager.NATIVE_HEIGHT;
					parent.setY(loopBackY + offset); // account for errors
				}
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}
		});
	}

}
