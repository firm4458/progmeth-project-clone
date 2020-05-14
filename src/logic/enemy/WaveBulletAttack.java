package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;
import logic.enemy.AttackScriptFactory.AttackScript;
import logic.enemy.RadialBulletAttack.RadialBulletAttackScript;

public class WaveBulletAttack extends AttackScriptFactory {

	private double offsetX;
	private double offsetY;
	private long duration;

	public WaveBulletAttack(double offsetX, double offsetY, long duration) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new WaveBulletAttackScript(offsetX, offsetY, duration);
	}

	public class WaveBulletAttackScript extends AttackScript {

		private double offsetX;
		private double offsetY;
		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 5;
		private static final int BASE_SPEED = 5;
		private double amplitude;
		private double frequency;

		private WaveBulletAttackScript(double offsetX, double offsetY, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			amplitude = Math.PI/3;
			frequency = 0.5;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
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
			.addGameObject(new BossCircularBullet(parent.getX()+offsetX, parent.getY()+offsetY, BASE_SPEED*Math.sin(angle), BASE_SPEED));
		}

	}

}
