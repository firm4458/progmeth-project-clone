package drawing;
import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.base.GameObject;

public class ImageSprite extends Sprite {
	
	private Image image;
	
	public ImageSprite(GameObject parent, Image image) {
		super(parent);
		this.image = image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	
	@Override
	public void draw(GraphicsContext gc, Camera camera) {
		double absoluteX = parent.getX()+relativeX-camera.getX();
		double absoluteY = parent.getY()+relativeY-camera.getY();
		gc.setEffect(colorAdjust);
		gc.drawImage(image, absoluteX, absoluteY);
		gc.restore();
	}

	
}
