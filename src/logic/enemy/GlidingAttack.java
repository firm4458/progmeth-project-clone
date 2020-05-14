package logic.enemy;

import application.GameManager;
import logic.base.GameInterruptException;
import logic.enemy.AttackScriptFactory.AttackScript;
import logic.enemy.RadialBulletAttack.RadialBulletAttackScript;

public class GlidingAttack extends AttackScriptFactory {
	
	private double offsetX;
	private double offsetY;
	private long duration;
	private double glidingAmplitude;
	

	public GlidingAttack(double offsetX, double offsetY, double glidingAmplitude, long duration) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.glidingAmplitude = glidingAmplitude;
		this.duration = duration;
	}

	@Override
	public AttackScript createScript() {
		return new GlidingAttackScript(offsetX, offsetY, glidingAmplitude, duration);
	}

	public class GlidingAttackScript extends AttackScript {

		private long duration;
		private long start;
		private int counter;
		private static final int COOLDOWN = 10;
		private static final int BASE_SPEED = 10;
		private double offsetX;
		private double offsetY;
		private double originalX;
		private double glidingAmplitude;
		private double a;
		
		@Override 
		public void onAttach() {
			originalX = parent.getX();
		}

		private GlidingAttackScript(double offsetX, double offsetY,double glidingAmplitude, long duration) {
			start = System.currentTimeMillis();
			this.duration = duration;
			counter = COOLDOWN;
			this.glidingAmplitude = glidingAmplitude;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		@Override
		public void update() throws GameInterruptException {
			long now = System.currentTimeMillis();
			if (now - start <= duration) {
				long t = now-start;
				//System.out.println(glidingAmplitude*Math.sin(2*Math.PI*t/duration));
				parent.setX(originalX+glidingAmplitude*Math.sin(2*Math.PI*t/duration));
				if (counter == 0) {
					counter = COOLDOWN;
					createBullet(0,BASE_SPEED);
				} else {
					counter--;
				}
			} else {
				parent.setX(originalX);
				setDone(true);
			}
		}
		
		private void createBullet(double speedX, double speedY) {
			GameManager.getInstance().getCurrentScene()
			.addGameObject(new BossCircularBullet(parent.getX()+offsetX, parent.getY()+offsetY, speedX, speedY));
		}

	}
}
