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

	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;

	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs, Media bgm) {
		super(name,bgm);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
	}

	@Override
	public void init() {
		
		super.init();

		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(new EnemySpawner(Meteor.meteorFactory, enemyGroup, Meteor.METEOR_SPAWN_STRATEGY));
		if(planetImgs.size()>0) {
			spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		}
		addGameObject(spawner);

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
	}
}
