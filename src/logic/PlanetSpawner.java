package logic;

import java.util.ArrayList;

import application.GameManager;
import drawing.ImageSprite;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.GameObjectFactory;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

public class PlanetSpawner extends BasicScript<GameObject> {

	private GameObject planet;
	private int count;
	private int cooldown;
	private ArrayList<Image> planetImgs;
	private double speed;

	private final GameObjectFactory<GameObject> planetFactory = new GameObjectFactory<GameObject>() {

		private int prevIndex = -1;

		@Override
		public GameObject createGameObject() {
			int index = 0;
			do {
				index = (int) (Math.random() * planetImgs.size());
			} while (index == prevIndex);
			prevIndex = index;
			Image img = planetImgs.get(index);
			double scale = Math.random() * 0.4 + 0.6;
			GameObject planet = new GameObject(Math.random() * GameManager.NATIVE_WIDTH - img.getWidth() * scale / 2,
					-img.getHeight() * scale);
			planet.setSprite(new ImageSprite(planet, img));
			((ImageSprite) planet.getSprite()).setScale(scale);
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(-0.15);
			planet.getSprite().addEffect("dark", effect);
			planet.getSprite().setZ(-88);
			planet.addScript(new ConstantSpeedMove(0, speed));
			planet.addScript(new ColliderBox(img.getWidth(), img.getHeight()));
			planet.addScript(new AutoRemove(30));
			return planet;
		}

	};

	public PlanetSpawner(ArrayList<Image> planetImgs, double speed, int cooldown) {
		this.planetImgs = planetImgs;
		this.speed = speed;
		planet = planetFactory.createGameObject();
		GameManager.getInstance().getCurrentScene().addGameObject(planet);
		this.cooldown = cooldown;
		count = cooldown;
	}

	@Override
	public void update() {
		if (planet.isDestroyed()) {
			if ((count--) <= 0) {
				planet = planetFactory.createGameObject();
				GameManager.getInstance().getCurrentScene().addGameObject(planet);
				count = cooldown;
			}
		}
	}

}
