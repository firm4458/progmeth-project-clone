package application;

import java.util.ArrayList;

import drawing.ImageSprite;
import drawing.Sprite;
import javafx.scene.image.Image;
import javafx.util.Pair;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.GroupOfMeteors;
import logic.player.Player;
import logic.util.ConstantSpeedMove;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;

public class NormalLevelScene extends GameScene {
	public GroupOfMeteors groupOfMeteors;

	public NormalLevelScene() {
		super();
	}

	@Override
	public void init() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
		
		isDestroyed = false;
		groupOfMeteors = new GroupOfMeteors();

		GameObject planetSpawn = new GameObject(0, 0);
		planetSpawn.addScript(new PlanetSpawner());
		addGameObject(planetSpawn);

		GameObject background = new GameObject(0, -420);
		Image img = new Image("img/parallax-space-backgound.png", 600, 1020, true, true);
		Sprite bgSprite = new ImageSprite(background, img);
		bgSprite.setZ(-99);
		background.setSprite(bgSprite);
		background.addScript(new ConstantSpeedMove(0, 0.07));

		background.addScript(new Script() {

			private GameObject parent;
			private double a;

			@Override
			public void update() {
				parent.getSprite().getColorAdjust().setBrightness(0.1 * Math.sin(a));
				a += 0.02;
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}

			@Override
			public void onDestroy() {

			}

		});
		addGameObject(background);

		Player player = new Player(250, 400);

		addGameObject(player);
		addGameObject(groupOfMeteors);
	}
}
