package logic.enemy;

import java.util.TreeMap;

import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class ExplosionAnimation extends GameObject {
	private static AnimationState animation;
	public Animator animator;
	private static Image[] img;
	static {
		img = new Image[] { new Image("img/explosion1.png", 100, 100, true, true),
				new Image("img/explosion1.png", 100, 100, true, true),
				new Image("img/explosion2.png", 100, 100, true, true),
				new Image("img/explosion2.png", 100, 100, true, true),
				new Image("img/explosion3.png", 100, 100, true, true),
				new Image("img/explosion3.png", 100, 100, true, true),
				new Image("img/explosion4.png", 100, 100, true, true),
				new Image("img/explosion4.png", 100, 100, true, true),
				new Image("img/explosion5.png", 100, 100, true, true),
				new Image("img/explosion5.png", 100, 100, true, true),
				new Image("img/explosion6.png", 100, 100, true, true),
				new Image("img/explosion6.png", 100, 100, true, true),
				new Image("img/explosion7.png", 100, 100, true, true),
				new Image("img/explosion7.png", 100, 100, true, true),
				new Image("img/explosion8.png", 100, 100, true, true),
				new Image("img/explosion8.png", 100, 100, true, true),
				new Image("img/explosion9.png", 100, 100, true, true),
				new Image("img/explosion9.png", 100, 100, true, true) };
	}

	public ExplosionAnimation(double x, double y) {
		this(x, y, 1);
	}

	/**
	 * Construct the explosion animation by create animation script
	 * @param x This is x position
	 * @param y This is y position
	 * @param scale This is a scale for the picture
	 */
	public ExplosionAnimation(double x, double y, double scale) {
		super(x, y);
		sprite = new ImageSprite(this, new Image("img/explosion9.png", 100, 100, true, true));
		sprite.setRelativeX(-50 * scale);
		sprite.setRelativeY(-50 * scale);
		((ImageSprite) sprite).setScale(scale);
		animation = new AnimationState("idle", img, new TreeMap<String, AnimationState>()) {
			@Override
			public void onEnd() {
				getAnimator().getParent().destroy();
			}
		};
		animator = new Animator((ImageSprite) sprite, animation);
		animation.setAnimator(animator);
		addScript(animator);
	}
}
