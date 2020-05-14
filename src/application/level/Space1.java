package application.level;

import java.util.ArrayList;

import application.NormalLevelScene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.base.GameObject;
import logic.enemy.FodderEnemies;
import logic.enemy.spawner.EnemySpawner;
import logic.util.ResourceManager;

public class Space1 extends NormalLevelScene {

	private static Image background = ResourceManager.getImage("background1");
	private static ArrayList<Image> planetImgs = new ArrayList<Image>();
	static {
		planetImgs.add(ResourceManager.getImage("planet.dark"));
		planetImgs.add(ResourceManager.getImage("planet.ring"));
	}

	public Space1() {
		this("space1", ResourceManager.getSound("sound/normal.mp3"));
	}

	public Space1(String name, Media bgm) {
		super(name, background, planetImgs, bgm);
	}

	@Override
	public void init() {
		super.init();
		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(
				new EnemySpawner(FodderEnemies.meteorFactory, enemyGroup, FodderEnemies.FODDER_SPAWN_STRATEGY));
	}

}
