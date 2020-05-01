package logic.player;

import java.util.ArrayList;

import application.GUI;
import application.GameManager;
import application.NormalLevelScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.enemy.ExplosionAnimation;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;
import logic.util.ConstantSpeedMove;


public class Bullet extends GameObject {
	private int damage = 10;
	
	public Bullet(double X, double Y) {
		super(X,Y);
		Image img = new Image("img/bullet1.png", 50, 50, true, true);
		sprite = new ImageSprite(this, img);
		addScript(new ConstantSpeedMove(0,-5)).
		addScript(new ColliderBox(img.getWidth(), img.getHeight())).
		addScript(new AutoRemove()).
		addScript(new CollisionDetection(((NormalLevelScene)GameManager.getInstance().getCurrentScene()).groupOfMeteors.getMeteors()) {
			
			@Override
			public void onCollision(ArrayList<GameObject> targets) {
				for(GameObject target: targets) {
					if(!target.isDestroyed()) {
						target.destroy();
						parent.destroy();
						GUI.sampleScene.addGameObject(new ExplosionAnimation(target.getX(), target.getY()));
						GameManager.getInstance().setScore(GameManager.getInstance().getScore()+10);
						return;
					}
				}
			}
		});
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}

}
