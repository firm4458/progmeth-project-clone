package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;
import logic.enemy.AttackScriptFactory.AttackScript;
import logic.enemy.RadialBulletAttack.RadialBulletAttackScript;

public class WaveBulletAttack extends AttackScriptFactory {

	private double originX;
	private double originY;
	private long duration;

	public WaveBulletAttack(double originX, double originY, long duration) {
		this.originX = originX;
		this.originY = originY;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new WaveBulletAttackScript(originX, originY, duration);
	}

	public class WaveBulletAttackScript extends AttackScript {

		private double originX;
		private double originY;
		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 5;
		private static final int BASE_SPEED = 5;
		private double amplitude;
		private double frequency;

		private WaveBulletAttackScript(double originX, double originY, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			amplitude = Math.PI/3;
			frequency = 0.5;
			this.originX = originX;
			this.originY = originY;
		}

		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration) {
				if (counter == 0) {
					counter = COOLDOWN;
					double angle = amplitude*Math.sin(2*Math.PI*frequency*((now-start)/1000.0)); // simple harmonic oscillation
					createBullet(angle); 
				} else {
					counter--;
				}
			} else {
				setDone(true);
			}
		}
		
		private void createBullet(double angle) {
			GameManager.getInstance().getCurrentScene()
			.addGameObject(new BossCircularBullet(originX, originY, BASE_SPEED*Math.sin(angle), BASE_SPEED));
		}

	}

}
