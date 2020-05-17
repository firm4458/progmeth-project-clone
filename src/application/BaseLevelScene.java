package application;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.LevelResultScene.LevelResult;
import drawing.ImageSprite;
import drawing.TextSprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.TextObject;
import logic.TextSkillObject;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.item.GroupOfItems;
import logic.player.Player;
import logic.util.DataManager;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;

/**
 * This is base class for every level
 */
public class BaseLevelScene extends GameScene {

	protected Media bgm; // scene's bgm
	protected AudioClip bgmPlayer; // player which plays bgm
	protected boolean isBoss; // whether or not this Scene is boss scene

	// getter and setter for isBoss
	public boolean isBoss() {
		return isBoss;
	}

	public void setBoss(boolean isBoss) {
		this.isBoss = isBoss;
	}

	/*
	 * create a base level scene with bgm as background music
	 * 
	 */
	public BaseLevelScene(String name, Media bgm) {
		super(name);
		this.bgm = bgm;
		bgmPlayer = new AudioClip(bgm.getSource());
		bgmPlayer.setCycleCount(AudioClip.INDEFINITE);
	}

	/*
	 * create a base level Scene with default bgm
	 */
	public BaseLevelScene(String name) {
		this(name, ResourceManager.getSound("sound/normal.mp3"));
	}

	// GameObject group which contains all enemy
	protected GameObjectGroup enemyGroup;

	// getter for enemyGroup
	public GameObjectGroup getEnemyGroup() {
		return enemyGroup;
	}

	// current score
	protected int score;

	// getter for score
	public int getScore() {
		return score;
	}

	// add point to score
	public void addScore(int point) {
		score += point;
	}

	// group of items
	protected GroupOfItems groupOfItems;
	
	public GroupOfItems getGroupOfItems() {
		return groupOfItems;
	}

	/*
	 * initialize scene with pause button, health bar, skill icon, player
	 */
	@Override
	public void init() {
		
		// initialize groups
		enemyGroup = createGroup(); 
		groupOfItems = new GroupOfItems();

		// create player
		Player player = new Player(260, 400);

		// add player and groupOfItems to scene
		addGameObject(player);
		addGameObject(groupOfItems);
		
		createSkillIcon(player);
		
		// create text for score
		GameObject scoreText = new TextObject(0, 85, "Score: 0", new Font("Comic Sans MS", 28), 500);
		TextSprite sprite = (TextSprite) scoreText.getSprite();
		sprite.setCenterAligned(false);
		scoreText.getSprite().setZ(99);
		
		// create a script that willl update text every frame
		scoreText.addScript(new BasicScript<GameObject>() {

			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite) (parent.getSprite());
				ts.setText("Score: " + getScore());
			}

		});
		addGameObject(scoreText);
		
		createHealthBar(player);
		
		createButtons();

		bgmPlayer.play();

	}
	
	/*
	 * creates skill icon and duration text for dash and the world
	 */
	private void createSkillIcon(Player player) {
		
		// create dash icon
		GameObject dashSkillIcon = new GameObject(10, 540);
		dashSkillIcon.setSprite(new ImageSprite(dashSkillIcon, new Image("img/dash skill.png", 50, 50, true, true)));
		dashSkillIcon.getSprite().setZ(99);
		addGameObject(dashSkillIcon);
		
		// create dash cooldown / duration text
		GameObject dashSkillTime = new TextSkillObject(65, 540, "DashText", "READY", new Font("ARCADECLASSIC", 20), 100,
				player);
		addGameObject(dashSkillTime);

		// create the world icon
		GameObject theWorldSkillIcon = new GameObject(100, 540);
		theWorldSkillIcon
				.setSprite(new ImageSprite(theWorldSkillIcon, new Image("img/The World.png", 50, 50, true, true)));
		theWorldSkillIcon.getSprite().setZ(99);
		addGameObject(theWorldSkillIcon);

		// create the world cooldown / duration text
		GameObject theWorldSkillTime = new TextSkillObject(155, 540, "TheWorldText", "READY",
				new Font("ARCADECLASSIC", 20), 100, player);
		addGameObject(theWorldSkillTime);
	}
	
	/*
	 * create player's health bar
	 */
	private void createHealthBar(Player player) {
		// create health bar
		GameObject healthBar = new GameObject(0, 0);
		healthBar.setSprite(new ImageSprite(healthBar, ResourceManager.getImage("healthBar")));
		healthBar.getSprite().setZ(98);
		addGameObject(healthBar);

		// create red bar indicating current health
		GameObject healthPortion = new GameObject(56, 24);
		healthPortion.setSprite(new ImageSprite(healthPortion, ResourceManager.getImage("health")));
		healthPortion.getSprite().setZ(99);
		healthPortion.addScript(new BasicScript<GameObject>() {
			private double originalWidth;
			private double maxHealth;

			@Override
			public void onAttach() {
				// set original width and player's max health
				originalWidth = parent.getSprite().getWidth();
				maxHealth = player.getStatus().getMaxHealth();
			}

			@Override
			public void lateUpdate() {
				// set width of the bar proportional to current health
				ImageSprite sprite = (ImageSprite) parent.getSprite();
				double portion = player.getStatus().getHealth() / maxHealth;
				sprite.setWidth(originalWidth * portion);
			}
		});
		addGameObject(healthPortion);
	}
	
	/*
	 * create pause, resume, leave buttons
	 */
	private void createButtons() {
		ImageButton pause = new ImageButton(50, 50, ResourceManager.getImage("button.pause"), null, null); // create pause button
		ImageButton cont = new ImageButton(50, 50, ResourceManager.getImage("button.resume"), null, null); // create resume button
		ImageButton leave = new ImageButton(50, 50, ResourceManager.getImage("button.exit"), null, null); // create leave button
		
		// move buttons
		pause.getGameObject().setX(GameManager.NATIVE_WIDTH - 50);
		cont.getGameObject().setX(GameManager.NATIVE_WIDTH - 50);
		leave.getGameObject().setX(GameManager.NATIVE_WIDTH - 100);
		
		// resume and leave button are disabled at first
		cont.disable();
		leave.disable();
		
		GameManager gameManager = GameManager.getInstance();
		GameScene scene = this;
		
		pause.setOnAction((evt) -> {
			/*
			 * when the pause button is pressed
			 * disable pause button
			 * enable resume and leave buttons
			 * signal game pause event to game manager
			 */
			cont.enable();
			leave.enable();
			pause.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_PAUSE, null));
		});
		cont.setOnAction((evt) -> {
			/*
			 * when the resume button is pressed
			 * disable resume and leave buttons
			 * enable pause button
			 * signal game resume event to game manager
			 */
			pause.enable();
			leave.disable();
			cont.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_RESUME, null));
		});
		leave.setOnAction((evt) -> {
			/*
			 * when the leave button is pressed
			 * add score to total score
			 * write new total score
			 * signal scene change event to game manager
			 */
			int totalScore = 0;
			if (DataManager.getInstance().contains("totalScore")) {
				totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
			}
			gameManager.signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
					new Pair<String, Object>("totalScore", totalScore + ((BaseLevelScene) scene).getScore())));
			gameManager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE,
					new LevelResultScene("select", LevelResult.NONE, ((BaseLevelScene) scene).getScore())));
		});
		
		// add all buttons to 
		Group root = (Group) getRoot();
		root.getChildren().addAll(pause, cont, leave);
	}

	@Override
	public void destroy() {
		super.destroy();
		bgmPlayer.stop();
	}

}
