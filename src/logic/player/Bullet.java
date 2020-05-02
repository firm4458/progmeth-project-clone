package logic.player;

import java.util.ArrayList;

import application.GUI;
import application.GameManager;
import application.NormalLevelScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.Projectile;
import logic.base.Entity;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.ExplosionAnimation;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;
import logic.util.ConstantSpeedMove;
import logic.util.GameObjectGroup;
import logic.util.ResourceManager;

public class Bullet extends Projectile {

	private int damage = 10;
	private static final Image img = ResourceManager.getImage("bullet");

	public Bullet(double X, double Y) {

		super(X, Y, 1, new ConstantSpeedMove(0, -5), new ColliderBox(img.getWidth(), img.getHeight()),
				((NormalLevelScene) GameManager.getInstance().getCurrentScene()).groupOfMeteors.getMeteors(), 0);

		sprite = new ImageSprite(this, img);

	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	protected void actOn(Entity target) {
		target.getStatus().takeDamage(getDamage());
	}

}
