package logic.util.scripts;

import javafx.geometry.BoundingBox;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public class ColliderBox implements Script {

	private GameObject parent;
	private double width;
	private double height;
	private double relativeX;
	private double relativeY;

	public ColliderBox(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public ColliderBox(double relativeX, double relativeY, double width, double height) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.width = width;
		this.height = height;
	}

	public BoundingBox getBound() {
		return new BoundingBox(parent.getX() + relativeX, parent.getY() + relativeY, width, height);
	}

	@Override
	public void update() {

	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		this.parent = parent;
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

	public double getRelativeX() {
		return relativeX;
	}

	public void setRelativeX(double relativeX) {
		this.relativeX = relativeX;
	}

	public double getRelativeY() {
		return relativeY;
	}

	public void setRelativeY(double relativeY) {
		this.relativeY = relativeY;
	}

}
