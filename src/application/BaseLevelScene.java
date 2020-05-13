package application;

import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import drawing.ImageSprite;
import drawing.TextSprite;
import gui.ImageButton;
import javafx.scene.Group;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
	protected MediaPlayer bgmPlayer;
	
	public BaseLevelScene(String name, Media bgm) {
		super(name);
		this.bgm = bgm;
		bgmPlayer = new MediaPlayer(bgm);
		bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	public BaseLevelScene(String name) {
		this(name,ResourceManager.getSound("sound/normal.mp3"));
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

		GameObject scoreText = new TextObject(300, 40, "Score: 0", new Font("Comic Sans MS", 35), 500);
		GameObject healthText = new TextObject(50, 40, "X" + player.getStatus().getHealth(),
				new Font("Comic Sans MS", 35), 100);
		scoreText.getSprite().setZ(99);
		healthText.getSprite().setZ(99);
		scoreText.addScript(new BasicScript<GameObject>() {

			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite) (parent.getSprite());
				ts.setText("Score: " + getScore());
			}

		});
		healthText.addScript(new BasicScript<GameObject>() {
			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite) (parent.getSprite());
				ts.setText("X" + player.getStatus().getHealth());
			}
		});
		addGameObject(scoreText);
		addGameObject(healthText);

		GameObject heart = new GameObject(0, 0);
		heart.setSprite(new ImageSprite(heart, ResourceManager.getImage("heart")));
		heart.getSprite().setZ(98);
		addGameObject(heart);
		
		ImageButton pause = new ImageButton(50, 50, ResourceManager.getImage("button.pause"), null, null);
		ImageButton cont = new ImageButton(50,50,ResourceManager.getImage("button.resume"),null,null);
		pause.getGameObject().setX(GameManager.NATIVE_WIDTH-50);
		cont.getGameObject().setX(GameManager.NATIVE_WIDTH-50);
		cont.disable();
		GameManager gameManager = GameManager.getInstance();
		GameScene scene = this;
		pause.setOnAction((evt)->{
			cont.enable();
			pause.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_PAUSE, null));
			System.out.println("pause");
		});
		cont.setOnAction((evt)->{
			pause.enable();
			cont.disable();
			gameManager.signalEvent(new GameEvent(scene, GameEventType.GAME_RESUME, null));
			System.out.println("resume");
		});
		Group root = (Group)getRoot();
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
