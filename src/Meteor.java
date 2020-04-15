import javafx.scene.paint.Color;

public class Meteor extends Sprite implements Moveable{
	
	public Meteor(int width, int height, Color color) {
		super(width, height, color);
	}
	
	public void move() {
		this.setTranslateY(this.getTranslateY()+3);
	}
	
	public boolean checkOutOfPane() {
		return this.getTranslateY() > 600;
	}
	
}
