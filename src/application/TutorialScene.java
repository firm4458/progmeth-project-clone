package application;

import java.util.ArrayList;
import java.util.TreeMap;

import drawing.ImageSprite;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.TextObject;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.enemy.Meteor;
import logic.enemy.spawner.EnemySpawner;
import logic.util.InputUtil;
import logic.util.ResourceManager;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class TutorialScene extends BaseLevelScene {

	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;
	protected MediaPlayer bgmPlayer;
	protected String bgmUrl = "sound/normal.mp3";

	public TutorialScene(String name, Image backgroundImage, ArrayList<Image> planetImgs) {
		super(name);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
	}

	@Override
	public void init() {
		super.init();

		GameObject spawner = new GameObject(0, 0, "spawner");
		Script enemySpawner = new EnemySpawner(Meteor.meteorFactory, enemyGroup, Meteor.METEOR_SPAWN_STRATEGY);
		spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		addGameObject(spawner);

		GameObject tutorial = new GameObject(0, 0);
		addGameObject(tutorial);
		Script movementTutorial = new BasicScript<GameObject>() {
			private GameObject[] buttons;
			private TextObject text;

			@Override
			public void onAttach() {
				String[] path = new String[] {"button.keys.UP","button.keys.UP","button.keys.UP","button.keys.UP"};
				buttons = new GameObject[4];
				for(int i=0;i<4;++i) {
					buttons[i] = new GameObject(0,0);
					ImageSprite sprite = new ImageSprite(buttons[i], ResourceManager.getImage(path[i]));
					sprite.setScale(0.2);
					sprite.setZ(99);
					buttons[i].setSprite(sprite);
					Image[] frames = new Image[20];
					for (int j = 0; j < 10; ++j) {
						frames[j] = ResourceManager.getImage(path[i]);
					}
					for (int j = 10; j < 20; ++j) {
						frames[j] = ResourceManager.getImage(path[i]+".pressed");
					}
					buttons[i].addScript(new Animator(sprite,
							new AnimationState("idle", frames, new TreeMap<String, AnimationState>())));
					parent.getScene().addGameObject(buttons[i]);
				}
				buttons[0].translate(270, 200);
				buttons[1].translate(270, 270);
				buttons[2].translate(200, 270);
				buttons[3].translate(340, 270);
				
				text = new TextObject(300, 350, "Press Arrow Keys to move", new Font("ARCADECLASSIC",18), 600);
				text.getSprite().setZ(99);
				parent.getScene().addGameObject(text);
			}

			@Override
			public void update() {
				if (InputUtil.isKeyPressed(KeyCode.UP) || InputUtil.isKeyPressed(KeyCode.DOWN)
						|| InputUtil.isKeyPressed(KeyCode.LEFT) || InputUtil.isKeyPressed(KeyCode.RIGHT)) {
					parent.removeScript(this);
					parent.getScene().getGameObject("spawner").addScript(enemySpawner);
				}
			}

			@Override
			public void onDestroy() {
				for(GameObject button : buttons) {
					button.destroy();
				}
				text.destroy();
			}
		};
		tutorial.addScript(movementTutorial);

		/*
		 * GameObject background = new GameObject(0, -420,"background"); Image img = new
		 * Image("img/parallax-space-backgound.png", GameManager.NATIVE_WIDTH+10, 0,
		 * true, true); Sprite bgSprite = new ImageSprite(background, img);
		 * bgSprite.setZ(-99); background.setSprite(bgSprite); background.addScript(new
		 * ConstantSpeedMove(0, 0.07)); background.addScript(new Script() {
		 * 
		 * private GameObject parent; private double a;
		 * 
		 * @Override public void update() {
		 * parent.getSprite().getColorAdjust().setBrightness(0.1 * Math.sin(a)); a +=
		 * 0.02; }
		 * 
		 * @Override public GameObject getParent() { return parent; }
		 * 
		 * @Override public void setParent(GameObject parent) throws
		 * IncompatibleScriptException { this.parent = parent; }
		 * 
		 * @Override public void onDestroy() {
		 * 
		 * }
		 * 
		 * }); addGameObject(background);
		 */

		// using three background object so that background loop seamlessly
		ScriptFactory factory = new ScriptFactory() {
			@Override
			public Script createScript() {
				return new BasicScript<GameObject>() {
					private double a;

					@Override
					public void update() {
						parent.getSprite().getColorAdjust().setBrightness(0.07 * Math.sin(a));
						a += 0.02;
					}
				};
			}
		};
		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		arr.add(factory);
		LoopBackground.createLoopBackground(this, backgroundImage, 0.07, 3, arr);

		bgmPlayer = new MediaPlayer(ResourceManager.getSound(bgmUrl));
		bgmPlayer.play();
	}

}
