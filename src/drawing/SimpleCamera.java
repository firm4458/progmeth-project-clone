package drawing;

import application.GameManager;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import logic.base.GameObject;
import logic.base.ScriptNotFoundException;
import logic.util.scripts.ColliderBox;

public class SimpleCamera extends GameObject {
	private Canvas canvas;

	public SimpleCamera(Canvas canvas) {
		super(0, 0);
		this.canvas = canvas;
		this.name = "camera";
	}

	public boolean isInCamera(GameObject gameObject) {
		BoundingBox bound = new BoundingBox(X, Y, GameManager.NATIVE_WIDTH, GameManager.NATIVE_HEIGHT);
		try {
			BoundingBox targetBound = gameObject.getScript(ColliderBox.class).getBound();
			return bound.intersects(targetBound);
		} catch (ScriptNotFoundException e) {
			return bound.contains(gameObject.getX(), gameObject.getY());
		}
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
