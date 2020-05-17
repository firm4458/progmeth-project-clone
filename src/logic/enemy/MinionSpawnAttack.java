package logic.enemy;

import java.util.ArrayList;

import application.BaseLevelScene;
import application.GameManager;
import logic.base.ScriptFactory;
import logic.util.ResourceManager;
import logic.util.scripts.factory.AutoRemoveFactory;

public class MinionSpawnAttack extends AttackScriptFactory {

	private ScriptFactory minionMovementFactory;
	private long duration;
	private double relativeX;
	private double relativeY;

	public MinionSpawnAttack(double relativeX, double relativeY, long duration, ScriptFactory minionMovementFactory) {
		super();
		this.minionMovementFactory = minionMovementFactory;
		this.duration = duration;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	@Override
	public AttackScript createScript() {
		return new MinionSpawnAttackScript(duration, relativeX, relativeY, minionMovementFactory);
	}

	public class MinionSpawnAttackScript extends AttackScript {
		private long start;
		private long duration;
		private static final long COOLDOWN = 600;
		private long coolDownStart;
		private double relativeX;
		private double relativeY;
		private ScriptFactory minionMovementFactory;

		public MinionSpawnAttackScript(long duration, double relativeX, double relativeY,
				ScriptFactory minionMovementFactory) {
			start = System.currentTimeMillis();
			this.duration = duration;
			this.relativeX = relativeX;
			this.relativeY = relativeY;
			this.minionMovementFactory = minionMovementFactory;
			coolDownStart = -999999;
		}

		/**
		 * construct minion
		 */
		@Override
		public void update() {
			long now = System.currentTimeMillis();
			if (now - start > duration) {
				setDone(true);
			} else if (now - coolDownStart > COOLDOWN) {
				Enemy minion = minionFactory.createGameObject();
				minion.setX(parent.getX() + relativeX);
				minion.setY(parent.getY() + relativeY);
				minion.addScript(minionMovementFactory.createScript());
				coolDownStart = now;
				parent.getScene().addGameObject(minion);
				parent.getScene().addGameObject(minion, ((BaseLevelScene) parent.getScene()).getEnemyGroup());
			}
		}
	}

	/**
	 * this factory is used for create enemy
	 */
	public static final EnemyFactory minionFactory = new EnemyFactory();
	static {
		minionFactory.setHealth(100);
		minionFactory.setDamage(10);
		minionFactory.setImage(ResourceManager.getImage("minion"));
		System.out.println(ResourceManager.getImage("minion").getWidth());
		minionFactory.setPoint(10);
		ArrayList<ScriptFactory> scriptFactories = minionFactory.getScriptFactories();
		scriptFactories.add(new AutoRemoveFactory(20));
		minionFactory.setOnHitPlayerFunc((targets, enemy) -> {
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene()
					.addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
	}

}
