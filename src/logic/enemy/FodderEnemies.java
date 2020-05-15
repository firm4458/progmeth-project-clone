package logic.enemy;

import java.util.ArrayList;
import java.util.Map;

import application.GameManager;
import drawing.ImageSprite;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.base.ScriptNotFoundException;
import logic.enemy.spawner.SpawnStrategy;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.factory.AutoRemoveFactory;
import logic.util.scripts.factory.ConstantSpeedMoveFactory;
import logic.util.scripts.factory.RotateScriptFactory;

public class FodderEnemies {
	
	public static class FodderSpawnStrategy implements SpawnStrategy{
		private int max_count;
		private long cooldown;
		private static final String strategyName = "FODDER_SPAWN_STRATEGY";
		
		public FodderSpawnStrategy(int max_count, long cooldown) {
			this.max_count = max_count;
			this.cooldown = cooldown;
		}

		@Override
		public boolean checkConditionSpawn(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {

			if (!data.containsKey(strategyName)) {
				// initialization, always spawn a meteor
				data.put(strategyName, System.currentTimeMillis());
				return true;
			}

			long now = System.currentTimeMillis();
			long prev = (long) data.get(strategyName);
			if (now - prev <= cooldown || group.size() > max_count) {
				return false;
			}
			return true;
		}

		@Override
		public void customizeFactory(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {
			factory.setX((GameManager.NATIVE_WIDTH - 20) * Math.random() + 20);
			factory.setY(-50 * Math.random() - 10);
			data.put(strategyName, System.currentTimeMillis());
		}
	}

	public static final SpawnStrategy FODDER_SPAWN_STRATEGY = new SpawnStrategy() {
		private static final int MAX_METEOR = 25;
		private static final long COOLDOWN = 100;
		private static final String strategyName = "FODDER_SPAWN_STRATEGY";

		@Override
		public boolean checkConditionSpawn(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {

			if (!data.containsKey(strategyName)) {
				// initialization, always spawn a meteor
				data.put(strategyName, System.currentTimeMillis());
				return true;
			}

			long now = System.currentTimeMillis();
			long prev = (long) data.get(strategyName);
			if (now - prev <= COOLDOWN || group.size() > MAX_METEOR) {
				return false;
			}
			return true;
		}

		@Override
		public void customizeFactory(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {
			factory.setX((GameManager.NATIVE_WIDTH - 20) * Math.random() + 20);
			factory.setY(-50 * Math.random() - 10);
			data.put(strategyName, System.currentTimeMillis());
		}
	};
	
	public static final SpawnStrategy SPACE_SHIP_FODDER_SPAWN_STRATEGY = new SpawnStrategy() {
		private static final int MAX_SHIP = 5;
		private static final long COOLDOWN = 500;
		private static final String strategyName = "SPACE_SHIP_SPAWN_STRATEGY";

		@Override
		public boolean checkConditionSpawn(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {

			if (!data.containsKey(strategyName)) {
				// initialization, always spawn a meteor
				data.put(strategyName, System.currentTimeMillis());
				return true;
			}

			long now = System.currentTimeMillis();
			long prev = (long) data.get(strategyName);
			if (now - prev <= COOLDOWN || group.size() > MAX_SHIP) {
				return false;
			}
			return true;
		}

		@Override
		public void customizeFactory(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data) {
			
			factory.setY((GameManager.NATIVE_HEIGHT - 20) * Math.random() + 20);
			double rand = Math.random();
			
			ConstantSpeedMoveFactory scriptFactory = null;
			for(ScriptFactory sf : factory.getScriptFactories()) {
				if(sf instanceof ConstantSpeedMoveFactory) {
					scriptFactory = (ConstantSpeedMoveFactory) sf;
					break;
				}
			}

			RotateScriptFactory rotateScriptFactory = null;
			for(ScriptFactory sf : factory.getScriptFactories()) {
				if(sf instanceof RotateScriptFactory) {
					rotateScriptFactory = (RotateScriptFactory) sf;
					break;
				}
			}
			double speed = Math.abs(scriptFactory.getSpeedX());
			if(rand>0.5) {
				factory.setX(-100 * Math.random() - 10);
				scriptFactory.setSpeedX(speed);
				rotateScriptFactory.setRot(0);
			}else {
				factory.setX(GameManager.NATIVE_WIDTH + 20*Math.random());
				scriptFactory.setSpeedX(-speed);
				rotateScriptFactory.setRot(180);
			}
			data.put(strategyName, System.currentTimeMillis());
		}
	};

	public static final EnemyFactory meteorFactory;
	public static final EnemyFactory asteroidFactory;
	public static final EnemyFactory spaceShipFactory;

	static {
		meteorFactory = new EnemyFactory();
		meteorFactory.setHealth(10);
		meteorFactory.setDamage(1);
		meteorFactory.setImage(ResourceManager.getImage("meteor"));
		meteorFactory.setPoint(10);
		ArrayList<ScriptFactory> scriptFactories = meteorFactory.getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(0, 3.0));
		scriptFactories.add(new AutoRemoveFactory(20));
		meteorFactory.setOnHitPlayerFunc((targets, enemy) -> {
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene()
					.addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
		asteroidFactory = new EnemyFactory();
		asteroidFactory.setHealth(50);
		asteroidFactory.setDamage(2);
		asteroidFactory.setImage(ResourceManager.getImage("asteroid"));
		asteroidFactory.setPoint(30);
		scriptFactories = asteroidFactory.getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(0, 1.5));
		scriptFactories.add(new ScriptFactory() {
			@Override
			public Script createScript() {
				return new BasicScript<GameObject>() {
					private double degree = 0;

					@Override
					public void update() {
						ImageSprite imgSprite = (ImageSprite) parent.getSprite();
						imgSprite.setRotate(degree);
						degree += 1;
					}
				};
			}
		});
		scriptFactories.add(new AutoRemoveFactory(20));
		asteroidFactory.setOnHitPlayerFunc((targets, enemy) -> {
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene()
					.addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
		spaceShipFactory = new EnemyFactory() {
			@Override
			public Enemy createGameObject() {
				Enemy ship = super.createGameObject();
				try {
					ColliderBox collider = ship.getScript(ColliderBox.class);
					collider.setRelativeY(24);
					collider.setHeight(48);
				}catch(ScriptNotFoundException ex) {
					System.err.println("cant get collider box");
				}
				return ship;
			}
		};
		spaceShipFactory.setHealth(100);
		spaceShipFactory.setDamage(5);
		spaceShipFactory.setImage(ResourceManager.getImage("enemy.ship"));
		spaceShipFactory.setPoint(100);
		scriptFactories = spaceShipFactory.getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(4, 0));
		scriptFactories.add(new AutoRemoveFactory(20));
		scriptFactories.add(new RotateScriptFactory());
		spaceShipFactory.setOnHitPlayerFunc((targets, enemy) -> {
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene()
					.addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
	}

}
