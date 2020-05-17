package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;

public class RadialBulletAttack extends AttackScriptFactory {

	private double offsetX;
	private double offsetY;
	private long duration;
	private int damage;

	public RadialBulletAttack(double offsetX, double offsetY, long duration, int damage) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.duration = duration;
		this.damage = damage;
	}

	@Override
	public AttackScript createScript() {
		return new RadialBulletAttackScript(offsetX, offsetY, duration, damage);
	}

	public class RadialBulletAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 10;
		private static final int BASE_SPEED = 5;
		private double angleDifferent;
		private double offsetX;
		private double offsetY;
		private int damage;

		private RadialBulletAttackScript(double offsetX, double offsetY, long duration, int damage) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			angleDifferent = Math.PI / 6;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.damage = damage;
		}

		/**
		 * create bullet that have different angle by using angleDifferent fields to seperate them
		 */
		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration) {
				if (counter == 0) {
					counter = COOLDOWN;
					for (double theta = 0; theta <= Math.PI; theta += angleDifferent) {
						createBullet(BASE_SPEED * Math.cos(theta), BASE_SPEED * Math.sin(theta));
					}
				} else {
					counter--;
				}
			} else {
				setDone(true);
			}
		}

		private void createBullet(double speedX, double speedY) {
			GameManager.getInstance().getCurrentScene().addGameObject(
					new BossCircularBullet(parent.getX() + offsetX, parent.getY() + offsetY, speedX, speedY, damage));
		}

	}

}
