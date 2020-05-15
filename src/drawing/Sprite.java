package drawing;

import java.util.TreeMap;

import application.GameManager;
import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import logic.base.GameObject;

public class Sprite implements Renderable {

	protected double relativeX;
	protected double relativeY;
	protected boolean isVisible;
	protected double rotate;
	protected GameObject parent;
	protected int Z;
	protected final TreeMap<String, Effect> effects = new TreeMap<String, Effect>();

	public Sprite(GameObject parent) {
		relativeX = 0;
		relativeY = 0;
		isVisible = true;
		this.parent = parent;
	}

	public double getRotate() {
		return rotate;
	}

	public void setRotate(double rotate) {
		this.rotate = rotate;
	}

	@Override
	public void draw(GraphicsContext gc, SimpleCamera camera) {
		double absoluteX = parent.getX() + relativeX - camera.getX();
		double absoluteY = parent.getY() + relativeY - camera.getY();
		gc.setFill(Color.WHITE);
		effects.forEach((name, effect) -> gc.setEffect(effect));
		effects.clear();
		double XScale = gc.getCanvas().getWidth() / GameManager.NATIVE_WIDTH;
		double YScale = gc.getCanvas().getHeight() / GameManager.NATIVE_HEIGHT;
		gc.fillRect(absoluteX * XScale, absoluteY * YScale, 50 * XScale, 50 * YScale);
		gc.restore();
	}

	public void addEffect(String name, Effect e) {
		effects.put(name, e);
	}

	public void removeEffect(String name) {
		effects.remove(name);
	}

	public void clearEffect() {
		effects.clear();
	}

	@Override
	public boolean isDestroyed() {
		return parent.isDestroyed();
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	public double getWidth() {
		return 50;
	}

	public double getHeight() {
		return 50;
	}

	public void setZ(int Z) {
		this.Z = Z;
	}

	@Override
	public int getZ() {
		return Z;
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
