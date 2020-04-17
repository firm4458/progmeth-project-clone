package logic.player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import drawing.ImageSprite;
import drawing.Sprite;
import logic.base.GameObject;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class Player extends GameObject {
	
	private static AnimationState idleState;
	private static AnimationState goLeftState;
	private static AnimationState goRightState;
	
	static {
		Image fullimg = new Image("img/ship.png",400,240,true,true);
		Image[] images = new Image[20];
		int index = 0;
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,0,80,120);
		}
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,120,80,120);
		}
		idleState = new AnimationState("idle",new Image[] {images[2],images[2],images[7],images[7]}, 
				new TreeMap<String,AnimationState>());
		goLeftState = new AnimationState("goLeft",new Image[] {images[0],images[0],images[5],images[5]}, 
				new TreeMap<String,AnimationState>());
		goRightState = new AnimationState("goRight",new Image[] {images[4],images[4],images[9],images[9]}, 
				new TreeMap<String,AnimationState>());
		idleState.putTrigger("goLeft", goLeftState);
		idleState.putTrigger("goRight", goRightState);
		goRightState.putTrigger("idle", idleState);
		goRightState.putTrigger("goLeft", goLeftState);
		goLeftState.putTrigger("idle", idleState);
		goLeftState.putTrigger("goRight", goRightState);
	}

	public Player(double X, double Y) {
		super(X, Y);
		WritableImage img = new WritableImage(new Image("img/ship.png",400,240,true,true).getPixelReader(),0,0,80,120);
		sprite = new ImageSprite(this,img);
		animator = new Animator((ImageSprite)sprite,idleState);
		addScript(new PlayerController()).addScript(animator).addScript(new BulletShooter());
	}
	
	public Animator animator;

}
