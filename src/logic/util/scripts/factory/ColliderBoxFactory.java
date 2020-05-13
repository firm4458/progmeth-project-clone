package logic.util.scripts.factory;

import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.ColliderBox;

public class ColliderBoxFactory extends ScriptFactory {

	private double width;
	private double height;
	private double relativeX;
	private double relativeY;

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

	@Override
	public Script createScript() {
		return new ColliderBox(relativeX, relativeY, width, height);
	}

}
