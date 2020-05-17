package logic.player;

import application.GameManager;
import application.UpgradeScene;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.group.GameObjectGroup;

public class BulletShooter implements Script {

	GameObjectGroup bullets = GameManager.getInstance().getCurrentScene().createGroup();
	private static final int COOLDOWN = 10;
	private static final int MAX_BULLET = 100;
	private int cooldown = COOLDOWN;
	GameObject parent;

	/**
	 * Use to generate the bullet
	 */
	@Override
	public void update() {

		if (cooldown == 0 && MAX_BULLET > bullets.size()) {
			if (parent.getClass() == Player.class) {
				addBullet(parent.getX() + 30, parent.getY() - 50);
				if (((Player) parent).getUpgradeAmmo()) {
					addBullet(parent.getX() + 10, parent.getY() - 50);
					addBullet(parent.getX() + 50, parent.getY() - 50);
				}
			}
		}

		--cooldown;
		if (cooldown < 0) {
			cooldown = COOLDOWN;
		}

	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		for (GameObject bullet : bullets.getChildren()) {
			bullet.destroy();
		}
	}

	/**
	 * create bullet and add to game manager
	 * @param X This is a x position
	 * @param Y This is a y position
	 */
	public void addBullet(double X, double Y) {
		Bullet bullet = new Bullet(X, Y, UpgradeScene.calculateDamage());
		GameManager.getInstance().getCurrentScene().addGameObject(bullet);
		GameManager.getInstance().getCurrentScene().addGameObject(bullet, bullets);

	}
}
