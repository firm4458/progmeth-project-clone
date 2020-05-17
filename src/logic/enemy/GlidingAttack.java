package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;

public class GlidingAttack extends AttackScriptFactory {

	private double offsetX;
	private double offsetY;
	private long duration;
	private double glidingAmplitude;
	private int damage;

	public GlidingAttack(double offsetX, double offsetY, double glidingAmplitude, long duration, int damage) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.glidingAmplitude = glidingAmplitude;
		this.duration = duration;
		this.damage = damage;
	}

	@Override
	public AttackScript createScript() {
		return new GlidingAttackScript(offsetX, offsetY, glidingAmplitude, duration, damage);
	}

	public class GlidingAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 5;
		private static final int BASE_SPEED = 10;
		private double offsetX;
		private double offsetY;
		private double originalX;
		private double glidingAmplitude;
		private int damage;

		@Override
		public void onAttach() {
			originalX = parent.getX();
		}

		private GlidingAttackScript(double offsetX, double offsetY, double glidingAmplitude, long duration,
				int damage) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			this.glidingAmplitude = glidingAmplitude;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.damage = damage;
		}

		/**
		 * update by using gliding function to set x and also create bullet
		 */
		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration || Math.abs(parent.getX() - originalX) > 1) {
				long t = now - start;
				parent.setX(originalX + glidingAmplitude * Math.sin(4 * Math.PI * t / duration));
				if (counter == 0) {
					counter = COOLDOWN;
					createBullet(0, BASE_SPEED);
				} else {
					counter--;
				}
			} else {
				parent.setX(originalX);
				setDone(true);
			}
		}

		private void createBullet(double speedX, double speedY) {
			GameManager.getInstance().getCurrentScene().addGameObject(
					new BossCircularBullet(parent.getX() + offsetX, parent.getY() + offsetY, speedX, speedY, damage));
		}

	}
}
