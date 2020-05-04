package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;

public class RadialBulletAttack extends AttackScriptFactory {

	private double originX;
	private double originY;
	private long duration;

	public RadialBulletAttack(double originX, double originY, long duration) {
		this.originX = originX;
		this.originY = originY;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new RadialBulletAttackScript(originX, originY, duration);
	}

	public class RadialBulletAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 10;
		private static final int BASE_SPEED = 10;
		private double angleDifferent;

		private RadialBulletAttackScript(double originX, double originY, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			angleDifferent = Math.PI/6;
		}

		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration) {
				if (counter == 0) {
					counter = COOLDOWN;
					for(double theta=0;theta<=Math.PI;theta+=angleDifferent) {
						createBullet(BASE_SPEED*Math.cos(theta),BASE_SPEED*Math.sin(theta));
					}
				} else {
					counter--;
				}
			} else {
				setDone(true);
			}
		}
		
		private void createBullet(double speedX, double speedY) {
			GameManager.getInstance().getCurrentScene()
			.addGameObject(new BossCircularBullet(originX, originY, speedX, speedY));
		}

	}

}
