package logic.enemy;

import java.util.ArrayList;

import application.BaseLevelScene;
import application.GameManager;
import application.GameScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.Entity;
import logic.base.EntityStatus;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.player.Player;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.CollisionDetection;
import logic.util.scripts.DamageEffect;
import logic.util.scripts.DioShaker;
import logic.util.scripts.ShakerScript;
import java.util.function.*;

public class Enemy extends Entity {

	protected int point;
	protected boolean destroyOnDeath=false;
	protected BiConsumer<ArrayList<GameObject>,Enemy> onHitPlayerFunc=DEFAULT_ON_HIT_PLAYER;
	protected Consumer<Enemy> onDeathFunc=DEFAULT_ON_DEATH;
	

	public void setOnHitPlayerFunc(BiConsumer<ArrayList<GameObject>, Enemy> onHitPlayerFunc) {
		this.onHitPlayerFunc = onHitPlayerFunc;
	}

	public void setOnDeathFunc(Consumer<Enemy> onDeathFunc) {
		this.onDeathFunc = onDeathFunc;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public void onDeath() throws SceneChangeInterruptException {
		onDeathFunc.accept(this);
	}
	
	@Deprecated
	public Enemy(double X, double Y, int health, int damage, Script motionScript, Script attackScript, Image image) {
		super(X, Y, new EntityStatus(health) {
			@Override
			public void takeDamage(int damage) {
				super.takeDamage(damage);
				getParent().addScript(new DamageEffect(100,getParent()));
			}
		});
		getStatus().setDamage(damage);
		setSprite(new ImageSprite(this, image));
		if (motionScript != null) {
			addScript(motionScript);
		}
		if (attackScript != null) {
			addScript(attackScript);
		}
		addScript(new ColliderBox(image.getWidth(), image.getHeight()));
		addScript(new CollisionDetection(Player.playerGroup) {
			
			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				onHitPlayer(targets);
			}
		});
	}
	
	public Enemy(double X, double Y, int health, int damage, Image image) {
		super(X, Y, new EntityStatus(health) {
			@Override
			public void takeDamage(int damage) {
				super.takeDamage(damage);
				getParent().addScript(new DamageEffect(100,getParent()));
			}
		});
		getStatus().setDamage(damage);
		setSprite(new ImageSprite(this, image));
		addScript(new ColliderBox(image.getWidth(), image.getHeight()));
		addScript(new CollisionDetection(Player.playerGroup) {
			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				onHitPlayer(targets);
			}
		});
	}
	
	protected static final BiConsumer<ArrayList<GameObject>,Enemy> DEFAULT_ON_HIT_PLAYER = (targets,enemy)->{
		if(enemy.isDestroyed()) {
			return;
		}
		for(GameObject gameObj : targets) {
			if(gameObj.isDestroyed()) {
				continue;
			}
			Player player = (Player)gameObj;
			player.getStatus().takeDamage(enemy.getStatus().getDamage());
		}
	};
	
	protected static final Consumer<Enemy> DEFAULT_ON_DEATH = (enemy)->{
		BaseLevelScene scene = (BaseLevelScene)enemy.getScene();
		scene.addGameObject(new ExplosionAnimation(enemy.getX(), enemy.getY()));
		scene.addScore(enemy.getPoint());
		enemy.destroy();
	};
	
	protected void onHitPlayer(ArrayList<GameObject> targets) {
		onHitPlayerFunc.accept(targets, this);
	}
	
	

}
