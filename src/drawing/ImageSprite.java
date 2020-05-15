package drawing;

import application.GameManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import logic.base.GameObject;

public class ImageSprite extends Sprite {

	protected Image image;
	private double width;
	private double height;

	private double scale = 1.0;

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public ImageSprite(GameObject parent, Image image) {
		super(parent);
		this.setImage(image);
		width = image.getWidth();
		height = image.getHeight();
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void draw(GraphicsContext gc, SimpleCamera camera) {
		double absoluteX = parent.getX() + relativeX - camera.getX();
		double absoluteY = parent.getY() + relativeY - camera.getY();
		gc.save();
		Rotate r = new Rotate(rotate, absoluteX + getWidth() / 2, absoluteY + getHeight() / 2);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
		effects.forEach((name, effect) -> gc.setEffect(effect));
		double XScale = gc.getCanvas().getWidth() / GameManager.NATIVE_WIDTH;
		double YScale = gc.getCanvas().getHeight() / GameManager.NATIVE_HEIGHT;
		gc.drawImage(getImage(), absoluteX * XScale, absoluteY * YScale, getWidth() * XScale * scale,
				getHeight() * YScale * scale);
		gc.restore();
	}

	public Image getImage() {
		return image;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

}
