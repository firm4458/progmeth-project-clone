package drawing;
import application.GameManager;
import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.base.GameObject;

public class ImageSprite extends Sprite {
	
	protected Image image;
	
	private double scale=1.0;
	public double getScale() {
		return scale;
	}
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public ImageSprite(GameObject parent, Image image) {
		super(parent);
		this.setImage(image);
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	
	@Override
	public void draw(GraphicsContext gc, Camera camera) {
		double absoluteX = parent.getX()+relativeX-camera.getX();
		double absoluteY = parent.getY()+relativeY-camera.getY();
		gc.setEffect(colorAdjust);
		effects.forEach((name,effect)->gc.setEffect(effect));
		double XScale = gc.getCanvas().getWidth()/GameManager.NATIVE_WIDTH;
		double YScale = gc.getCanvas().getHeight()/GameManager.NATIVE_HEIGHT;
		gc.drawImage(getImage(), absoluteX*XScale, absoluteY*YScale,
				image.getWidth()*XScale*scale,image.getHeight()*YScale*scale);
		gc.restore();
	}

	public Image getImage() {
		return image;
	}

	@Override
	public double getHeight() {
		return image.getHeight();
	}
	
	@Override
	public double getWidth() {
		return image.getWidth();
	}
	
	
	
}
