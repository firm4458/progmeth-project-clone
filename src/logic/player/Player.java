package logic.player;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import com.sun.media.jfxmediaimpl.platform.Platform;

import application.BaseLevelScene;
import application.GUI;
import application.GameManager;
import application.GameScene;
import application.MenuScene;
import application.NormalLevelScene;
import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.SimpleCamera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import gui.ImageButton;
import logic.base.BasicScript;
import logic.base.Dio;
import logic.base.Entity;
import logic.base.EntityStatus;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
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
	private static int MaxHealthPoint = 3;
	private static boolean upgradeAmmo = false;
	private static int upgradeTimeAmmo = 0;

	public static GameObjectGroup playerGroup;

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
		ImageButton button = new ImageButton(100, 100, ResourceManager.getImage("button"), null, null);
		GameScene scene = getScene();
		button.setOnAction((evt) -> {
			GameManager gameManager = GameManager.getInstance();
			int totalScore = 0;
			if(DataManager.getInstance().contains("totalScore")) {
				totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
			}
			gameManager.signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
					new Pair<String, Object>("totalScore", totalScore + ((BaseLevelScene) scene).getScore())));
			gameManager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE, new MenuScene("menu")));
		});
		button.getGameObject().setX(250);
		button.getGameObject().setY(250);
		scene.addGUIElement(button);
		destroy();
	}

	public Player(double X, double Y) {
		super(X, Y, new EntityStatus(MaxHealthPoint) {
			MediaPlayer mediaPlayer;

			@Override
			public void takeDamage(int damage) {
				if (!isInvincible) {
					super.takeDamage(damage);
					Renderer.getInstance().getCamera().addScript(new ShakerScript(4, 1.2));
					mediaPlayer = new MediaPlayer(ResourceManager.getSound("sound/Boss hit 1.wav"));
					mediaPlayer.play();
				}
			}
		});
		playerGroup = GameManager.getInstance().getCurrentScene().createGroup();
		GameManager.getInstance().getCurrentScene().addGameObject(this, playerGroup);
		sprite = new ImageSprite(this, ResourceManager.getImage("idle1"));
		animator = new Animator((ImageSprite) sprite, idleState);
		addScript(new PlayerController()).addScript(animator).addScript(new BulletShooter())
				.addScript(new ColliderBox(10, 5, 60, 100));
		addScript(new Dash(this));
		addScript(new TheWorld(this));
		BaseLevelScene scene = (BaseLevelScene) GameManager.getInstance().getCurrentScene();

		// Collide with Meteors
		/*
		 * addScript(new CollisionDetection(scene.groupOfMeteors.getMeteors()) {
		 * 
		 * @Override public void onCollision(ArrayList<GameObject> targets) throws
		 * GameInterruptException { Renderer.getInstance().getCamera().setShake(true);
		 * for(GameObject meteor: targets) { meteor.destroy(); HealthPoint--; }
		 * if(HealthPoint <= 0) throw new SceneChangeInterruptException(new
		 * MenuScene()); } });
		 */
		////////

		// Collide with Items
		addScript(new CollisionDetection(scene.groupOfItems.getItems()) {

			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				for (GameObject item : targets) {
					if (!item.isDestroyed() && item.getClass() == Item.class) {
						String itemName = ((Item) item).getItemName();
						switch (itemName) {
						case "Powerup_Health":
							getStatus().heal(1);
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
					if (upgradeTimeAmmo == 0)
						upgradeAmmo = false;
				}
			}
		});
		//////
	}

	public void healing(int heal) {
		if (HealthPoint < MaxHealthPoint) {
			HealthPoint += heal;
		}
	}

	public int getHealthPoint() {
		return HealthPoint;
	}

	public int getMaxHealthPoint() {
		return MaxHealthPoint;
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

	public Animator animator;

}
