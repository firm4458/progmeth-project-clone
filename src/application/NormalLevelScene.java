package application;

import java.util.ArrayList;
import drawing.SimpleCamera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.TextSprite;
import drawing.base.Renderable;
import gui.GameButton;
import gui.util.ButtonScript;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.TextObject;
import drawing.TextSprite;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.enemy.GroupOfMeteors;
import logic.enemy.Meteor;
import logic.enemy.MeteorFactory;
import logic.enemy.spawner.EnemySpawner;
import logic.item.GroupOfItems;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.ConstantSpeedMove;

public class NormalLevelScene extends BaseLevelScene {
	public GroupOfItems groupOfItems;

	protected MediaPlayer bgmPlayer;
	protected String bgmUrl = "sound/normal.mp3";
	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;

	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs) {
		super(name);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
	}

	@Override
	public void init() {
		
		super.init();

		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(new EnemySpawner(Meteor.meteorFactory, enemyGroup, Meteor.METEOR_SPAWN_STRATEGY));
		spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		addGameObject(spawner);

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
		LoopBackground.createLoopBackground(this,backgroundImage, 0.07, 3,arr);

		bgmPlayer = new MediaPlayer(ResourceManager.getSound(bgmUrl));
		
		bgmPlayer.play();
	}

	@Override
	public void destroy() {
		bgmPlayer.stop();
		super.destroy();
	}
}
