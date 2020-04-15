import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Player extends Sprite implements Moveable, Renderable{
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private boolean moveUp = false;
	private boolean moveDown = false;
	private int MAX_BULLETS = 10;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	
	public Player(int width, int height, Color color) {
		super(width, height, color);
	}
	
	public void move() {
		if(this.moveLeft && this.getTranslateX() > 0) {
			this.setTranslateX(this.getTranslateX()-4);
		}
		if(this.moveRight && this.getTranslateX() < 600-this.getWidth()) {
			this.setTranslateX(this.getTranslateX()+4);
		}
		if(this.moveUp && this.getTranslateY() > 0) {
			this.setTranslateY(this.getTranslateY()-4);
		}
		if(this.moveDown && this.getTranslateY() < 600-this.getHeight()) {
			this.setTranslateY(this.getTranslateY()+4);
		}
	}
	
	
	public void shoot(Pane root) {
		if(bullets.size() < MAX_BULLETS) {
			Bullet temp = new Bullet((int)(this.getTranslateX() + this.getWidth()/2 - 5), (int)(this.getTranslateY()-15), 10, 20, Color.DARKBLUE);
			bullets.add(temp);
		}
	}
	
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}
	
	public void setMoveLeft(boolean x) {
		this.moveLeft = x;
	}

	public void setMoveRight(boolean x) {
		this.moveRight = x;
	}
	
	public void setMoveUp(boolean x) {
		this.moveUp = x;
	}
	
	public void setMoveDown(boolean x) {
		this.moveDown = x;
	}
	
	public void update(Pane root) {
		this.move();
		
		for(int i = 0; i < bullets.size() ; i++) {
			bullets.get(i).move();
			if(bullets.get(i).checkOutOfPane()) {
				bullets.get(i).setDead(true);
				bullets.remove(i);
			}
		}
		
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(getFill());
		gc.fillRect(getTranslateX(), getTranslateY(), getWidth(), getHeight());
	}
}
