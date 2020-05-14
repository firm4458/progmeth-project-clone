package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;

public class RandomBulletAttack extends AttackScriptFactory {

	private double offsetX;
	private double offsetY;
	private long duration;

	public RandomBulletAttack(double offsetX, double offsetY, long duration) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new RandomBulletAttackScript(offsetX, offsetY, duration);
	}

	public class RandomBulletAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private double offsetX;
		private double offsetY;
		private static final int COOLDOWN = 10;
		private static final int BASE_SPEED = 5;

		private RandomBulletAttackScript(double offsetX, double offsetY, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration) {
				if (counter == 0) {
					counter = COOLDOWN;
					createBullet(BASE_SPEED*Math.random(), BASE_SPEED);
					createBullet(BASE_SPEED*Math.random()*-1, BASE_SPEED);
				} else {
					counter--;
				}
			} else {
				setDone(true);
			}
		}
		
		private void createBullet(double speedX, double speedY) {
			GameManager.getInstance().getCurrentScene()
			.addGameObject(new BossCircularBullet(parent.getX()+offsetX, parent.getY()+offsetY, speedX, speedY));
		}

	}

}
