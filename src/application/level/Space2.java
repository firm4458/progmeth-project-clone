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

public class Space2 extends NormalLevelScene {

	private static Image background = ResourceManager.getImage("background2");
	private static ArrayList<Image> planetImgs = new ArrayList<Image>();
	static {
		planetImgs.add(ResourceManager.getImage("lavaPlanet"));
		planetImgs.add(ResourceManager.getImage("oceanPlanet"));
		planetImgs.add(ResourceManager.getImage("forestPlanet"));
		planetImgs.add(ResourceManager.getImage("icePlanet"));
	}

	public Space2() {
		this("space2", ResourceManager.getSound("sound/normal.mp3"));
	}

	public Space2(String name, Media bgm) {
		super(name, background, planetImgs, bgm);
	}

	@Override
	public void init() {
		super.init();
		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(
				new EnemySpawner(FodderEnemies.asteroidFactory, enemyGroup, new FodderSpawnStrategy(25, 200)));
		addGameObject(spawner);
	}

}
