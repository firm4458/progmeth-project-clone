package logic.enemy;

import java.util.TreeMap;

import drawing.ImageSprite;
import drawing.Renderer;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class ExplosionAnimation extends GameObject{
	private static AnimationState animation;
	private int count = 0;
	public Animator animator;
	static {
		Image[] img = {new Image("img/explosion1.png", 100, 100, true, true), new Image("img/explosion1.png", 100, 100, true, true), new Image("img/explosion2.png", 100, 100, true, true), new Image("img/explosion2.png", 100, 100, true, true), 
				new Image("img/explosion3.png", 100, 100, true, true), new Image("img/explosion3.png", 100, 100, true, true), new Image("img/explosion4.png", 100, 100, true, true), new Image("img/explosion4.png", 100, 100, true, true),
				new Image("img/explosion5.png", 100, 100, true, true), new Image("img/explosion5.png", 100, 100, true, true), new Image("img/explosion6.png", 100, 100, true, true), new Image("img/explosion6.png", 100, 100, true, true),
				new Image("img/explosion7.png", 100, 100, true, true), new Image("img/explosion7.png", 100, 100, true, true), new Image("img/explosion8.png", 100, 100, true, true), new Image("img/explosion8.png", 100, 100, true, true),
				new Image("img/explosion9.png", 100, 100, true, true), new Image("img/explosion9.png", 100, 100, true, true)};
		animation = new AnimationState("idle",img, 
				new TreeMap<String,AnimationState>());
	}
	
	public ExplosionAnimation(double x, double y){
		super(x,y);
		sprite = new ImageSprite(this, new Image("img/explosion9.png", 100, 100, true, true));
		animator = new Animator((ImageSprite)sprite,animation) {
			@Override
			public void update() {
				
				((ImageSprite) sprite).setImage(getCurrentImage());
				if(animation.getIndex() == 18) {
					getParent().destroy();
					Renderer.clearDestroyedRenderable();
				}
			}
		};
		addScript(animator);
	}
}
