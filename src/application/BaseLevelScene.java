package application;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.ImageSprite;
import drawing.TextSprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import logic.TextObject;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.item.GroupOfItems;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;

public class BaseLevelScene extends GameScene {

	protected Media bgm;
	protected AudioClip bgmPlayer;

	public BaseLevelScene(String name, Media bgm) {
		super(name);
		this.bgm = bgm;
		bgmPlayer = new AudioClip(bgm.getSource());
		bgmPlayer.setCycleCount(AudioClip.INDEFINITE);
	}

	public BaseLevelScene(String name) {
		this(name, ResourceManager.getSound("sound/normal.mp3"));
	}

	protected GameObjectGroup enemyGroup;

	public GameObjectGroup getEnemyGroup() {
		return enemyGroup;
	}

	protected int score;

	public int getScore() {
		return score;
	}

	public void addScore(int point) {
		score += point;
	}

	public GroupOfItems groupOfItems;

	@Override
	public void init() {
		enemyGroup = createGroup();
		groupOfItems = new GroupOfItems();

		Player player = new Player(260, 400);

		addGameObject(player);
		addGameObject(groupOfItems);

		GameObject scoreText = new TextObject(0, 85, "Score: 0", new Font("Comic Sans MS", 28), 500);
		TextSprite sprite = (TextSprite) scoreText.getSprite();
		sprite.setCenterAligned(false);
		scoreText.getSprite().setZ(99);
		scoreText.addScript(new BasicScript<GameObject>() {

			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite) (parent.getSprite());
				ts.setText("Score: " + getScore());
			}

		});
		addGameObject(scoreText);
		GameObject healthBar = new GameObject(0, 0);
		healthBar.setSprite(new ImageSprite(healthBar, ResourceManager.getImage("healthBar")));
		healthBar.getSprite().setZ(98);
		addGameObject(healthBar);

		GameObject healthPortion = new GameObject(56, 24);
		healthPortion.setSprite(new ImageSprite(healthPortion, ResourceManager.getImage("health")));
		healthPortion.getSprite().setZ(99);
		healthPortion.addScript(new BasicScript<GameObject>() {
			private double originalWidth;
			private double maxHealth;

			@Override
			public void onAttach() {
				originalWidth = parent.getSprite().getWidth();
				maxHealth = player.getStatus().getMaxHealth();
			}

			@Override
			public void lateUpdate() {
				ImageSprite sprite = (ImageSprite) parent.getSprite();
				double portion = player.getStatus().getHealth() / maxHealth;
				sprite.setWidth(originalWidth * portion);
			}
		});
		addGameObject(healthPortion);

		ImageButton pause = new ImageButton(50, 50, ResourceManager.getImage("button.pause"), null, null);
		ImageButton cont = new ImageButton(50, 50, ResourceManager.getImage("button.resume"), null, null);
		pause.getGameObject().setX(GameManager.NATIVE_WIDTH - 50);
		cont.getGameObject().setX(GameManager.NATIVE_WIDTH - 50);
		cont.disable();
		GameManager gameManager = GameManager.getInstance();
		GameScene scene = this;
		pause.setOnAction((evt) -> {
			cont.enable();
			pause.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_PAUSE, null));
		});
		cont.setOnAction((evt) -> {
			pause.enable();
			cont.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_RESUME, null));
		});
		Group root = (Group) getRoot();
		root.getChildren().add(pause);
		root.getChildren().add(cont);

		bgmPlayer.play();
	}

	@Override
	public void destroy() {
		super.destroy();
		bgmPlayer.stop();
	}

}
