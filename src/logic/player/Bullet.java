package logic.player;

import application.BaseLevelScene;
import application.GameManager;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.Projectile;
import logic.base.Dio;
import logic.base.Entity;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

public class Bullet extends Projectile implements Dio {

	private int damage = 10;
	private static final Image img = ResourceManager.getImage("bullet");

	/**
	 * Speed is -5 in y-axis
	 * @param X This is a x position
	 * @param Y This is a y position
	 * @param damage This is a damage of this bullet
	 */
	public Bullet(double X, double Y, int damage) {

		super(X, Y, 1, new ConstantSpeedMove(0, -5), new ColliderBox(img.getWidth(), img.getHeight()),
				((BaseLevelScene) GameManager.getInstance().getCurrentScene()).getEnemyGroup(), 0);
		this.damage = damage;
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
