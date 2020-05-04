package logic.enemy;

import application.GameManager;
import application.NormalLevelScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.Projectile;
import logic.base.Entity;
import logic.player.Player;
import logic.util.ColliderBox;
import logic.util.ConstantSpeedMove;
import logic.util.ResourceManager;

public class BossCircularBullet extends Projectile {
	

	private int damage = 1;
	private static final Image img = ResourceManager.getImage("bossBullet");

	public BossCircularBullet(double X, double Y, double speedX, double speedY) {

		super(X, Y, 1, new ConstantSpeedMove(speedX,speedY), new ColliderBox(img.getWidth(), img.getHeight()),
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
