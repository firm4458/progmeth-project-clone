package logic.enemy;

import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.Projectile;
import logic.base.Entity;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

public class BossCircularBullet extends Projectile {

	private int damage;
	private static final Image img = ResourceManager.getImage("bossBullet");

	/**
	 * use to contruct 
	 * @param X this is x position
	 * @param Y this is y position
	 * @param speedX this is speed in x-axis
	 * @param speedY this is speed in y-axis
	 * @param damage this is damage
	 */
	public BossCircularBullet(double X, double Y, double speedX, double speedY, int damage) {

		super(X, Y, 1, new ConstantSpeedMove(speedX, speedY), new ColliderBox(img.getWidth(), img.getHeight()),
				Player.playerGroup, 0);
		sprite = new ImageSprite(this, img);
		this.damage = damage;

	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * target take damage from this bullet
	 */
	@Override
	protected void actOn(Entity target) {
		target.getStatus().takeDamage(getDamage());
	}

}
