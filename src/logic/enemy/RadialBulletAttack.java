package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;

public class RadialBulletAttack extends AttackScriptFactory {

	private double offsetX;
	private double offsetY;
	private long duration;
	
	

	public RadialBulletAttack(double offsetX, double offsetY, long duration) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new RadialBulletAttackScript(offsetX, offsetY, duration);
	}

	public class RadialBulletAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 10;
		private static final int BASE_SPEED = 10;
		private double angleDifferent;
		private double offsetX;
		private double offsetY;

		private RadialBulletAttackScript(double offsetX, double offsetY, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			angleDifferent = Math.PI/6;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
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
			.addGameObject(new BossCircularBullet(parent.getX()+offsetX, parent.getY()+offsetY, speedX, speedY));
		}

	}

}
