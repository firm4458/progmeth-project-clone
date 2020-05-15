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

public class Space3 extends NormalLevelScene {

	private static Image background = ResourceManager.getImage("background3");
	private static ArrayList<Image> planetImgs = new ArrayList<Image>();
	static {
		planetImgs.add(ResourceManager.getImage("planet.dark"));
		planetImgs.add(ResourceManager.getImage("planet.ring"));
		planetImgs.add(ResourceManager.getImage("lavaPlanet"));
		planetImgs.add(ResourceManager.getImage("oceanPlanet"));
		planetImgs.add(ResourceManager.getImage("forestPlanet"));
		planetImgs.add(ResourceManager.getImage("icePlanet"));
	}

	public Space3(String name, Media bgm) {
		super(name, background, planetImgs, bgm);
	}

	public Space3() {
		this("space3", ResourceManager.getSound("sound/normal.mp3"));
	}

	@Override
	public void init() {
		super.init();
		GameObject spawner = new GameObject(0, 0);
		spawner.addScript(
				new EnemySpawner(FodderEnemies.asteroidFactory, enemyGroup, new FodderSpawnStrategy(15, 100)));
		spawner.addScript(new EnemySpawner(FodderEnemies.spaceShipFactory, enemyGroup,
				FodderEnemies.SPACE_SHIP_FODDER_SPAWN_STRATEGY));
		addGameObject(spawner);
	}

}
