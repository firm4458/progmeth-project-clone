import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Sprite extends Rectangle{
	private boolean dead;
	
	public Sprite(int width, int height, Color Color) {
		super(width, height, Color);
		this.dead = false;
	}
	
	public void setDead(boolean i) {
		this.dead = i;
	}
	
	public boolean getDead() {
		return this.dead;
	}
}
