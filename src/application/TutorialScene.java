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
import logic.util.scripts.AutoDestroy;

public class TutorialScene extends BaseLevelScene {

	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;

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

		Tutorial tutorial = new Tutorial(enemySpawner);
		addGameObject(tutorial);
		tutorial.startTutorial();

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
	}
}
