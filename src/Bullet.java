import javafx.scene.paint.Color;

public class Bullet extends Sprite implements Moveable {
	
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
}
