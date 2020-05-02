package application;

import java.util.ArrayList;
import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.TextSprite;
import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.PlanetSpawner;
import logic.TextObject;
import drawing.TextSprite;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.GroupOfMeteors;
import logic.item.GroupOfItems;
import logic.player.Player;
import logic.util.ConstantSpeedMove;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;
import logic.util.ResourceManager;

public class NormalLevelScene extends GameScene {
	public GroupOfMeteors groupOfMeteors;
	public GroupOfItems groupOfItems;
	private MediaPlayer mediaPlayer;

	public NormalLevelScene() {
		super();
	}

	@Override
	public void init() {
		
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
		
		addGameObject(new Camera(Renderer.getInstance().getGc().getCanvas()));
		
		isDestroyed = false;
		groupOfMeteors = new GroupOfMeteors();
		groupOfItems = new GroupOfItems();
		
		addGameObject(Renderer.getInstance().getCamera());
		
		GameObject planetSpawn = new GameObject(0, 0);
		planetSpawn.addScript(new PlanetSpawner());
		addGameObject(planetSpawn);

		GameObject background = new GameObject(0, -420);
		Image img = new Image("img/parallax-space-backgound.png", GameManager.NATIVE_WIDTH+10, 0, true, true);
		Sprite bgSprite = new ImageSprite(background, img);
		bgSprite.setZ(-99);
		background.setSprite(bgSprite);
		background.addScript(new ConstantSpeedMove(0, 0.07));
		background.addScript(new Script() {

			private GameObject parent;
			private double a;

			@Override
			public void update() {
				parent.getSprite().getColorAdjust().setBrightness(0.1 * Math.sin(a));
				a += 0.02;
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}

			@Override
			public void onDestroy() {

			}

		});
		addGameObject(background);

		Player player = new Player(250, 400);

		addGameObject(player);
		addGameObject(groupOfMeteors);
		addGameObject(groupOfItems);
		
		GameObject scoreText = new TextObject(300, 40, "Score: 0", new Font("Comic Sans MS", 35), 500);
		GameObject healthText = new TextObject(50,40,"X" + player.getHealthPoint(), new Font("Comic Sans MS", 35), 100);
		scoreText.getSprite().setZ(99);
		healthText.getSprite().setZ(99);
		scoreText.addScript(new Script() {
			GameObject parent;
			
			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite)(parent.getSprite());
				ts.setText("Score: " + GameManager.getInstance().getScore());
			}
			
			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}
			
			@Override
			public void onDestroy() {
				
			}
			
			@Override
			public GameObject getParent() {
				return parent;
			}
		});
		healthText.addScript(new Script() {
			GameObject parent;
			
			@Override
			public void update() throws GameInterruptException {
				TextSprite ts = (TextSprite)(parent.getSprite());
				ts.setText("X" + player.getHealthPoint());
			}
			
			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}
			
			@Override
			public void onDestroy() {
				
			}
			
			@Override
			public GameObject getParent() {
				return parent;
			}
		});
		addGameObject(scoreText);
		addGameObject(healthText);
		
		GameObject heart = new GameObject(0, 0);
		heart.setSprite(new ImageSprite(heart,ResourceManager.getImage("img/HealthPoint.png", 50, 50)));
		heart.getSprite().setZ(98);
		

		Media sound = new Media(ClassLoader.getSystemResource("sound/theme.mp3").toString());
		mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	@Override
	public void destroy() {
		mediaPlayer.stop();
		super.destroy();
	}
}
