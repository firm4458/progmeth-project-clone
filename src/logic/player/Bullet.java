package logic.player;

import java.util.ArrayList;

import application.BaseLevelScene;
import application.GUI;
import application.GameManager;
import application.NormalLevelScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.Projectile;
import logic.base.Dio;
import logic.base.Entity;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.ExplosionAnimation;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.CollisionDetection;
import logic.util.scripts.ConstantSpeedMove;

public class Bullet extends Projectile implements Dio {

	private int damage = 10;
	private static final Image img = ResourceManager.getImage("bullet");

	public Bullet(double X, double Y) {

		super(X, Y, 1, new ConstantSpeedMove(0, -5), new ColliderBox(img.getWidth(), img.getHeight()),
				((BaseLevelScene) GameManager.getInstance().getCurrentScene()).getEnemyGroup(), 0);

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
