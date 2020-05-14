package application;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.ScriptFactory;
import logic.enemy.FodderEnemies;
import logic.enemy.spawner.EnemySpawner;
import logic.util.scripts.factory.FlashingScriptFactory;

public class NormalLevelScene extends BaseLevelScene {

	protected Image backgroundImage;
	protected ArrayList<Image> planetImgs;
	protected static ScriptFactory flashing = new FlashingScriptFactory(0.02, 0.07);
	protected boolean isFlashing;

	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs, Media bgm,
			boolean isFlashing) {
		super(name, bgm);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
		this.isFlashing = isFlashing;
	}

	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs, Media bgm) {
		this(name, backgroundImage, planetImgs, bgm, true);
	}

	@Override
	public void init() {

		super.init();

		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(
				new EnemySpawner(FodderEnemies.meteorFactory, enemyGroup, FodderEnemies.FODDER_SPAWN_STRATEGY));
		if (planetImgs.size() > 0) {
			spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		}
		addGameObject(spawner);

		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		if (isFlashing) {
			arr.add(flashing);
		}
		// using three background object so that background loop seamlessly
		LoopBackground.createLoopBackground(this, backgroundImage, 0.07, 3, arr);
	}
}
