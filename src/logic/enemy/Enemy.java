package logic.enemy;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import application.BaseLevelScene;
import application.GameManager;
import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.LevelResultScene;
import application.LevelResultScene.LevelResult;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import javafx.util.Pair;
import logic.base.Entity;
import logic.base.EntityStatus;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.player.Player;
import logic.util.DataManager;
import logic.util.scripts.AutoDestroy;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.CollisionDetection;
import logic.util.scripts.DamageEffect;

public class Enemy extends Entity {

	protected int point;
	protected boolean destroyOnDeath = false;
	protected BiConsumer<ArrayList<GameObject>, Enemy> onHitPlayerFunc = DEFAULT_ON_HIT_PLAYER;
	protected Consumer<Enemy> onDeathFunc = DEFAULT_ON_DEATH;

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
	public void onDeath() {
		onDeathFunc.accept(this);
	}

	public Enemy(double X, double Y, int health, int damage, Image image) {
		super(X, Y, new EntityStatus(health) {
			@Override
			public void takeDamage(int damage) {
				super.takeDamage(damage);
				getParent().addScript(new DamageEffect(100, getParent()));
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

	/**
	 * function that use for make the player get the damage
	 */
	public static final BiConsumer<ArrayList<GameObject>, Enemy> DEFAULT_ON_HIT_PLAYER = (targets, enemy) -> {
		if (enemy.isDestroyed()) {
			return;
		}
		for (GameObject gameObj : targets) {
			if (gameObj.isDestroyed()) {
				continue;
			}
			Player player = (Player) gameObj;
			player.getStatus().takeDamage(enemy.getStatus().getDamage());
		}
	};

	/**
	 * function that destory enemy and create explosion animation and add score
	 */
	public static final Consumer<Enemy> DEFAULT_ON_DEATH = (enemy) -> {
		BaseLevelScene scene = (BaseLevelScene) enemy.getScene();
		scene.addGameObject(new ExplosionAnimation(enemy.getX() + enemy.getSprite().getWidth() / 2,
				enemy.getY() + enemy.getSprite().getHeight() / 2));
		scene.addScore(enemy.getPoint());
		enemy.destroy();
	};

	/**
	 * function that destory boss enemy and create explosion animation and add score and end the game
	 */
	public static final Consumer<Enemy> BOSS_ON_DEATH = (enemy) -> {
		BaseLevelScene scene = (BaseLevelScene) enemy.getScene();
		ImageSprite sprite = (ImageSprite) enemy.getSprite();
		GameManager manager = GameManager.getInstance();
		scene.addScore(enemy.getPoint());
		scene.addGameObject(new ExplosionAnimation(enemy.getX() + sprite.getWidth() / 2,
				enemy.getY() + sprite.getHeight() / 2, 10));
		GameObject gameObj = new GameObject(0, 0);
		gameObj.addScript(new AutoDestroy(1000) {
			@Override
			public void onDestroy() {
				int totalScore = 0;
				if (DataManager.getInstance().contains("totalScore")) {
					totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
				}
				manager.signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("totalScore", totalScore + scene.getScore())));
				manager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE,
						new LevelResultScene("result", LevelResult.WIN, scene.getScore())));
			}
		});
		scene.addGameObject(gameObj);
		enemy.destroy();
	};

	/**
	 * use onHitPlayerFunc 
	 * @param targets This is an input for onHitPlayerFunc.
	 */
	protected void onHitPlayer(ArrayList<GameObject> targets) {
		onHitPlayerFunc.accept(targets, this);
	}

}
