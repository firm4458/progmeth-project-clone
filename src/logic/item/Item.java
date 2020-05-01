package logic.item;

import java.util.Random;

import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.ConstantSpeedMove;

public class Item extends GameObject{
	private static Random rand = new Random();
	private static String[] PowerUp = {"Powerup_Health", "Powerup_Ammo"};
	private String itemName;
	private String url;
	
	public Item() {
		this(rand.nextDouble()*600, -30.0, PowerUp[new Random().nextInt(PowerUp.length)]);
	}
	
	public Item(double x, double y, String itemName) {
		super(x, y);
		switch(itemName) {
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
		addScript(new ConstantSpeedMove(0, 2)).
		addScript(new ColliderBox(sprite.getHeight(), sprite.getWidth())).
		addScript(new AutoRemove());
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
