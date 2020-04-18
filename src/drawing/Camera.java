package drawing;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import logic.base.GameObject;
import logic.base.ScriptNotFoundException;
import logic.util.ColliderBox;

public class Camera extends GameObject {
	private Canvas canvas;
	public Camera(Canvas canvas) {
		super(0,0);
		this.canvas = canvas;
	}
	public boolean isInCamera(GameObject gameObject) {
		double width = 600;
		double height = 600;
		BoundingBox bound = new BoundingBox(X,Y,width,height);
		try {
			BoundingBox targetBound = gameObject.getScript(ColliderBox.class).getBound();
			return bound.intersects(targetBound);
		}catch(ScriptNotFoundException e) {
			return bound.contains(gameObject.getX(),gameObject.getY());
		}
	}
}
