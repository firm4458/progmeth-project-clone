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

	private int damage = 10;
	private static final Image img = ResourceManager.getImage("bossBullet");

	public BossCircularBullet(double X, double Y, double speedX, double speedY) {

		super(X, Y, 1, new ConstantSpeedMove(speedX, speedY), new ColliderBox(img.getWidth(), img.getHeight()),
				Player.playerGroup, 0);
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
