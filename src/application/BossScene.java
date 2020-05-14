package application;

import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.Boss;
import logic.enemy.GroupOfMeteors;
import logic.enemy.Meteor;
import logic.enemy.TestAttack1;
import logic.enemy.TestAttack2;
import logic.enemy.spawner.EnemySpawner;

public class BossScene extends BaseLevelScene {
	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;
	protected AttackController controller;
	protected Boss boss;
	public BossScene(String name,Image backgroundImage, ArrayList<Image> planetImgs,Media bgm) {
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
		addGameObject(boss);
		addGameObject(boss,enemyGroup);
	}

}
