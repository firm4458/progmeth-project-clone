package application;

import java.util.ArrayList;
import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;
import logic.PlanetSpawner;
import logic.base.GameObject;
import logic.base.Script;
import logic.enemy.GroupOfMeteors;
import logic.item.GroupOfItems;
import logic.player.Player;
import logic.util.ConstantSpeedMove;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;

public class NormalLevelScene extends GameScene {
	public GroupOfMeteors groupOfMeteors;
	public GroupOfItems groupOfItems;

	public NormalLevelScene() {
		super();
	}

	@Override
	public void init() {
		
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
		
		addGameObject(new Camera(Renderer.getInstance().getGc().getCanvas()));
		
		isDestroyed = false;
		groupOfMeteors = new GroupOfMeteors();
		groupOfItems = new GroupOfItems();
		
		addGameObject(Renderer.getInstance().getCamera());
		
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
		addGameObject(groupOfItems);
		Renderer.getInstance().add(new Renderable() {

			@Override
			public void draw(GraphicsContext gc, Camera camera) {
				gc.drawImage(new Image("img/HealthPoint.png", 50, 50, true, true), 0, 0);
				gc.setFill(Color.WHITE);
				gc.setFont(new Font("Comic Sans MS", 35));
				gc.fillText("X" + player.getHealthPoint(), 50, 40);
				gc.fillText("Score: " + GameManager.getInstance().getScore(), 300, 40);
			}

			@Override
			public boolean isVisible() {
				return true;
			}

			@Override
			public boolean isDestroyed() {
				return false;
			}

			@Override
			public int getZ() {
				return 99;
			}
			
		});
	}
}
