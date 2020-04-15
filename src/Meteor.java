import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Meteor extends Sprite implements Moveable, Renderable{
	
	public Meteor(int width, int height, Color color) {
		super(width, height, color);
	}
	
	public void move() {
		this.setTranslateY(this.getTranslateY()+3);
	}
	
	public boolean checkOutOfPane() {
		return this.getTranslateY() > 600;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(getFill());
		gc.fillRect(getTranslateX(), getTranslateY(), getWidth(), getHeight());
	}
}
