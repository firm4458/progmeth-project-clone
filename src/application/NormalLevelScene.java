package application;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.LoopBackground;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.ScriptFactory;
import logic.util.scripts.factory.FlashingScriptFactory;


/*
 * This class inherit from Base level scene and add background and floating planets
 */
public class NormalLevelScene extends BaseLevelScene {

	// background image
	protected Image backgroundImage;
	// planet images
	protected ArrayList<Image> planetImgs;
	// flashing script factory for background
	protected static ScriptFactory flashing = new FlashingScriptFactory(0.02, 0.07);
	// whether or not background should be flashing
	protected boolean isFlashing;

	/*
	 * create a level scene with specified background image planet images and bgm
	 * if flashing is true, background will be flashing
	 */
	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs, Media bgm,
			boolean isFlashing) {
		super(name, bgm);
		this.backgroundImage = backgroundImage;
		this.planetImgs = planetImgs;
		this.isFlashing = isFlashing;
	}

	/*
	 * create a level scene with specified background image planet images and bgm
	 */
	public NormalLevelScene(String name, Image backgroundImage, ArrayList<Image> planetImgs, Media bgm) {
		this(name, backgroundImage, planetImgs, bgm, true);
	}

	/*
	 * initialize the scene
	 * first, use super.init()
	 * then, add spawner for floating planet and loop able background
	 */
	@Override
	public void init() {

		super.init();

		GameObject spawner = new GameObject(0, 0);
		if (planetImgs.size() > 0) {
			spawner.addScript(new PlanetSpawner(planetImgs, 1, 10));
		}
		addGameObject(spawner);

		ArrayList<ScriptFactory> arr = new ArrayList<ScriptFactory>();
		if (isFlashing) {
			arr.add(flashing);
		}
		// using three background object so that background loop seamlessly
		LoopBackground.createLoopBackground(this, backgroundImage, 0.15, 3, arr);
	}
}
