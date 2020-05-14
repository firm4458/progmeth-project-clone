package application;

import java.util.ArrayList;

import javafx.scene.image.Image;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.enemy.FodderEnemies;
import logic.enemy.spawner.EnemySpawner;
import logic.util.scripts.factory.FlashingScriptFactory;

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
		Script enemySpawner = new EnemySpawner(FodderEnemies.meteorFactory, enemyGroup,
				FodderEnemies.FODDER_SPAWN_STRATEGY);
		spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		addGameObject(spawner);

		Tutorial tutorial = new Tutorial(enemySpawner);
		addGameObject(tutorial);
		tutorial.startTutorial();

		// using three background object so that background loop seamlessly
		ScriptFactory factory = new FlashingScriptFactory(0.02, 0.07);
		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		arr.add(factory);
		LoopBackground.createLoopBackground(this, backgroundImage, 0.07, 3, arr);
	}
}
