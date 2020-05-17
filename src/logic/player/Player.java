package logic.player;

import java.util.ArrayList;
import java.util.TreeMap;

import application.BaseLevelScene;
import application.GameManager;
import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.GameScene;
import application.LevelResultScene;
import application.LevelResultScene.LevelResult;
import application.UpgradeScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.util.Pair;
import logic.base.BasicScript;
import logic.base.Dio;
import logic.base.Entity;
import logic.base.EntityStatus;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.ScriptNotFoundException;
import logic.enemy.ExplosionAnimation;
import logic.item.Item;
import logic.util.DataManager;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.CollisionDetection;
import logic.util.scripts.ShakerScript;

public class Player extends Entity implements Dio {

	private static AnimationState idleState;
	private static AnimationState goLeftState;
	private static AnimationState goRightState;
	private static int HealthPoint;
	private static int MaxHealthPoint = 10;
	private static boolean upgradeAmmo = false;
	private static int upgradeTimeAmmo = 0;

	public static GameObjectGroup playerGroup;

	/**
	 * initial idleState, goLeftState, and goRightState
	 */
	static {
		Image frame1 = ResourceManager.getImage("idle1");
		Image frame2 = ResourceManager.getImage("idle2");
		idleState = new AnimationState("idle", new Image[] { frame1, frame1, frame2, frame2 },
				new TreeMap<String, AnimationState>());

		frame1 = ResourceManager.getImage("left1");
		frame2 = ResourceManager.getImage("left2");
		goLeftState = new AnimationState("goLeft", new Image[] { frame1, frame1, frame2, frame2 },
				new TreeMap<String, AnimationState>());

		frame1 = ResourceManager.getImage("right1");
		frame2 = ResourceManager.getImage("right2");
		goRightState = new AnimationState("goRight", new Image[] { frame1, frame1, frame2, frame2 },
				new TreeMap<String, AnimationState>());

		idleState.putTrigger("goLeft", goLeftState);
		idleState.putTrigger("goRight", goRightState);
		goRightState.putTrigger("idle", idleState);
		goRightState.putTrigger("goLeft", goLeftState);
		goLeftState.putTrigger("idle", idleState);
		goLeftState.putTrigger("goRight", goRightState);
	}

	@Override
	public void onDeath() throws GameInterruptException {
		getScene().addGameObject(new ExplosionAnimation(getX(), getY()));
		GameScene scene = getScene();
		GameManager gameManager = GameManager.getInstance();
		int totalScore = 0;
		if (DataManager.getInstance().contains("totalScore")) {
			totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
		}
		gameManager.signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
				new Pair<String, Object>("totalScore", totalScore + ((BaseLevelScene) scene).getScore())));
		gameManager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE,
				new LevelResultScene("result", LevelResult.NONE, ((BaseLevelScene) scene).getScore())));
		destroy();
	}

	public Player(double X, double Y) {
		super(X, Y, new EntityStatus(UpgradeScene.calculateHealth()) {
			AudioClip mediaPlayer;

			@Override
			public void takeDamage(int damage) {
				if (!isInvincible) {
					super.takeDamage(damage);
					GameManager.getInstance().getCurrentScene().getSimpleCamera().addScript(new ShakerScript(4, 1.2));
					mediaPlayer = new AudioClip(ResourceManager.getSound("sound/Boss hit 1.wav").getSource());
					mediaPlayer.play();
				}
			}
		});
		upgradeAmmo = false;
		playerGroup = GameManager.getInstance().getCurrentScene().createGroup();
		GameManager.getInstance().getCurrentScene().addGameObject(this, playerGroup);
		sprite = new ImageSprite(this, ResourceManager.getImage("idle1"));
		animator = new Animator((ImageSprite) sprite, idleState);
		addScript(new PlayerController()).addScript(animator).addScript(new BulletShooter())
				.addScript(new ColliderBox(10, 5, 60, 80));
		addScript(new Dash(this));
		addScript(new TheWorld(this));
		BaseLevelScene scene = (BaseLevelScene) GameManager.getInstance().getCurrentScene();

		// Collide with Items
		addScript(new CollisionDetection(scene.getGroupOfItems().getItems()) {

			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				for (GameObject item : targets) {
					if (!item.isDestroyed() && item.getClass() == Item.class) {
						String itemName = ((Item) item).getItemName();
						switch (itemName) {
						case "Powerup_Health":
							getStatus().heal(UpgradeScene.calculateHealthItem());
							break;
						case "Powerup_Ammo":
							upgradeAmmo = true;
							upgradeTimeAmmo = 1800;
							break;
						}
						item.destroy();
					}
				}
			}

		});
		///////

		// Count UpgradeTimeAmmo
		addScript(new BasicScript<GameObject>() {

			@Override
			public void update() throws GameInterruptException {
				if (upgradeAmmo) {
					upgradeTimeAmmo--;
					if (upgradeTimeAmmo == 0) {
						upgradeAmmo = false;
					}
				}
			}
		});
		//////
		
		/**
		 * Check if it is boss stage inactive dash and the world skill
		 * Otherwise active them
		 */
		addScript(new BasicScript<GameObject>() {

			@Override
			public void update() throws GameInterruptException {
				if (!((BaseLevelScene) parent.getScene()).isBoss()) {
					try {
						parent.getScript(Dash.class).setActive(true);
						parent.getScript(TheWorld.class).setActive(true);
					} catch (ScriptNotFoundException e) {
						e.printStackTrace();
					}
					parent.removeScript(this);
				}
			}
		});
	}

	public void healing(int heal) {
		if (HealthPoint < MaxHealthPoint) {
			HealthPoint += heal;
		}
	}

	public void setUpgradeAmmo(boolean upgrade) {
		upgradeAmmo = upgrade;
	}

	public boolean getUpgradeAmmo() {
		return upgradeAmmo;
	}

	public int getUpgradeTimeAmmo() {
		return upgradeTimeAmmo;
	}

	public void setUpgradeTimeAmmo(int time) {
		upgradeTimeAmmo = time;
	}

	public long getDashSkillCooldown() {
		try {
			return getScript(Dash.class).getCooldown();
		} catch (ScriptNotFoundException e) {
			return -1;
		}
	}

	public boolean getDashSkillIsUsing() throws ScriptNotFoundException {
		return getScript(Dash.class).getUsingSkill();
	}

	public boolean getDashSkillIsActive() throws ScriptNotFoundException {
		return getScript(Dash.class).isActive();
	}

	public long getDashTime() {
		try {
			return getScript(Dash.class).getTimeDuration();
		} catch (ScriptNotFoundException e) {
			return -1;
		}
	}

	public boolean getTheWorldSkillIsUsing() throws ScriptNotFoundException {
		return getScript(TheWorld.class).getUsingSkill();
	}

	public boolean getTheWorldIsActive() throws ScriptNotFoundException {
		return getScript(TheWorld.class).isActive();
	}

	public long getTheWorldSkillCooldown() {
		try {
			return getScript(TheWorld.class).getCooldown();
		} catch (ScriptNotFoundException e) {
			return -1;
		}
	}

	public long getTheWorldTime() {
		try {
			return getScript(TheWorld.class).getTimeDuration();
		} catch (ScriptNotFoundException e) {
			return -1;
		}
	}

	public Animator animator;

	public void setSkillActive() {
		try {
			getScript(Dash.class).setActive(true);
			getScript(TheWorld.class).setActive(true);
		} catch (ScriptNotFoundException e) {
			e.printStackTrace();
		}
	}

}
