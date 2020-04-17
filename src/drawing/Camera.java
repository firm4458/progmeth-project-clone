package drawing;

import javafx.geometry.BoundingBox;
import logic.base.GameObject;
import logic.base.ScriptNotFoundException;
import logic.util.ColliderBox;

public class Camera extends GameObject {
	private double width;
	private double height;
	public Camera(double width, double height) {
		super(0,0);
		this.width = width;
		this.height = height;
	}
	public boolean isInCamera(GameObject gameObject) {
		BoundingBox bound = new BoundingBox(X,Y,width,height);
		try {
			BoundingBox targetBound = gameObject.getScript(ColliderBox.class).getBound();
			return bound.intersects(targetBound);
		}catch(ScriptNotFoundException e) {
			return bound.contains(gameObject.getX(),gameObject.getY());
		}
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
}
