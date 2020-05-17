package application.level;

import java.util.ArrayList;

import application.NormalLevelScene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.base.GameObject;
import logic.enemy.FodderEnemies;
import logic.enemy.FodderEnemies.FodderSpawnStrategy;
import logic.enemy.spawner.EnemySpawner;
import logic.util.ResourceManager;

public class Space1 extends NormalLevelScene {

	// default background image
	private static Image background = ResourceManager.getImage("background1");
	// planet images
	private static ArrayList<Image> planetImgs = new ArrayList<Image>();
	static {
		planetImgs.add(ResourceManager.getImage("planet.dark"));
		planetImgs.add(ResourceManager.getImage("planet.ring"));
	}

	/*
	 * create space1 level with deafult bgm
	 */
	public Space1() {
		this("space1", ResourceManager.getSound("sound/normal.mp3"));
	}

	/*
	 * create space 1
	 */
	public Space1(String name, Media bgm) {
		super(name, background, planetImgs, bgm);
	}

	@Override
	public void init() {
		super.init();
		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(new EnemySpawner(FodderEnemies.meteorFactory, enemyGroup, new FodderSpawnStrategy(25, 100)));
		addGameObject(spawner);
	}

}
