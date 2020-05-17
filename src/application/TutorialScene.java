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

/*
 * game scene for tutorial
 */
public class TutorialScene extends BaseLevelScene {

	// backgrond image
	protected Image backgroundImage;
	// planet images
	protected ArrayList<Image> planetImgs;

	/*
	 * create a tutorial scene with specified background image and planet images
	 */
	public TutorialScene(String name, Image backgroundImage, ArrayList<Image> planetImgs) {
		super(name);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
	}

	/*
	 * initialize the scene by invoking BaseLevelScene's init() and adding loopable background, floating planet, Tutorial game object
	 * then start the tutorial by using Tutorial's startTutorial method
	 */
	@Override
	public void init() {
		super.init();

		GameObject spawner = new GameObject(0, 0, "spawner");
		Script enemySpawner = new EnemySpawner(FodderEnemies.meteorFactory, enemyGroup,
				FodderEnemies.FODDER_SPAWN_STRATEGY);
		spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		addGameObject(spawner);

		Tutorial tutorial = new Tutorial();
		addGameObject(tutorial);
		tutorial.startTutorial();

		// using three background object so that background loop seamlessly
		ScriptFactory factory = new FlashingScriptFactory(0.02, 0.07);
		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		arr.add(factory);
		LoopBackground.createLoopBackground(this, backgroundImage, 0.07, 3, arr);
	}
}
