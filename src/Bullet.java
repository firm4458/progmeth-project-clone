import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends Sprite implements Moveable, Renderable {
	
	public Bullet(int X, int Y, int width, int height, Color color) {
		super(width, height, color);
		this.setTranslateX(X);
		this.setTranslateY(Y);
	}
	
	public void move() {
		this.setTranslateY(this.getTranslateY()-4);
	}
	
	public boolean checkOutOfPane() {
		return this.getTranslateY() < -this.getHeight();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(getFill());
		gc.fillRect(getTranslateX(), getTranslateY(), getWidth(), getHeight());
	}
}
