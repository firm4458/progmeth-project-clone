package logic.item;

import java.util.Random;

import application.GameManager;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

/**
 * <h1> Item! </h1>
 * rand is use to random powerup_health and powerup_ammo
 * Powerup contain itemname
 * @author user
 *
 */

public class Item extends GameObject {
	private static Random rand = new Random();
	private static String[] PowerUp = { "Powerup_Health", "Powerup_Ammo" };
	private String itemName;
	private String url;

	/**
	 * This construct will random item between powerup_health and powerup_ammo.
	 */
	public Item() {
		this(rand.nextDouble() * GameManager.NATIVE_WIDTH, -30.0, PowerUp[new Random().nextInt(PowerUp.length)]);
	}

	/**
	 * This construct create item according to itemName.
	 * @param x This is a position in x-axis
	 * @param y This is a positoin in y-axis
	 * @param itemName This is a name of the item
	 */
	public Item(double x, double y, String itemName) {
		super(x, y);
		switch (itemName) {
		case "Powerup_Health":
			url = "img/Powerup_Health.png";
			break;
		case "Powerup_Ammo":
			url = "img/Powerup_Ammo.png";
			break;
		}
		this.itemName = itemName;
		sprite = new ImageSprite(this, new Image(url, 48, 29, true, true));
		sprite.setZ(80);
		addScript(new ConstantSpeedMove(0, 2)).addScript(new ColliderBox(sprite.getHeight(), sprite.getWidth()))
				.addScript(new AutoRemove());
	}

	public String getItemName() {
		return itemName;
	}

	public String getURL() {
		return url;
	}

	public String[] getPowerUp() {
		return PowerUp;
	}
}
