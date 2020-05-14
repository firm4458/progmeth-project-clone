package logic.enemy;
import java.util.ArrayList;
import java.util.Map;

import application.BaseLevelScene;
import application.GameManager;
import application.NormalLevelScene;
import drawing.SimpleCamera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Projectile;
import logic.base.BasicScript;
import logic.base.Entity;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.base.ScriptNotFoundException;
import logic.enemy.spawner.SpawnStrategy;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;
import logic.util.scripts.factory.AutoRemoveFactory;
import logic.util.scripts.factory.ConstantSpeedMoveFactory;

public class FodderEnemies {
	
	public static final SpawnStrategy FODDER_SPAWN_STRATEGY = new SpawnStrategy() {
		private static final int MAX_METEOR = 25;
		private static final long COOLDOWN = 100;
		private static final String strategyName="METEOR_SPAWN_STRATEGY";
		
		@Override
		public boolean checkConditionSpawn(EnemyFactory factory, GameObjectGroup group, Map<String,Object> data) {
			
			if(!data.containsKey(strategyName)) {
				// initialization, always spawn a meteor
				data.put(strategyName,System.currentTimeMillis());
				return true;
			}
			
			long now = System.currentTimeMillis();
			long prev = (long) data.get(strategyName);
			if(now-prev<=COOLDOWN || group.size()>MAX_METEOR) {
				return false;
			}
			return true;
		}
		
		@Override
		public void customizeFactory(EnemyFactory factory, GameObjectGroup group, Map<String,Object> data) {
			factory.setX((GameManager.NATIVE_WIDTH-20)*Math.random() + 20);
			factory.setY(-50*Math.random()-10);
			data.put(strategyName, System.currentTimeMillis());
		}
	};
	
	public static final EnemyFactory meteorFactory;
	public static final EnemyFactory asteroidFactory;
	
	static {
		meteorFactory = new EnemyFactory();
		meteorFactory.setHealth(10);
		meteorFactory.setDamage(1);
		meteorFactory.setImage(ResourceManager.getImage("meteor"));
		meteorFactory.setPoint(10);
		ArrayList<ScriptFactory> scriptFactories = meteorFactory.getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(0,3.0));
		scriptFactories.add(new AutoRemoveFactory(20));
		meteorFactory.setOnHitPlayerFunc((targets,enemy)->{
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene().addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
		asteroidFactory = new EnemyFactory();
		asteroidFactory.setHealth(50);
		asteroidFactory.setDamage(2);
		asteroidFactory.setImage(ResourceManager.getImage("asteroid"));
		asteroidFactory.setPoint(30);
		scriptFactories = asteroidFactory.getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(0,1.5));
		scriptFactories.add(new ScriptFactory() {
			@Override
			public Script createScript() {
				return new BasicScript<GameObject>() {
					private double degree=0;
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
		asteroidFactory.setOnHitPlayerFunc((targets,enemy)->{
			Enemy.DEFAULT_ON_HIT_PLAYER.accept(targets, enemy);
			GameManager.getInstance().getCurrentScene().addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
			enemy.destroy();
		});
	}
	
}
