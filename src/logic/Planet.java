package logic;

import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.util.scripts.AutoRemove;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;

public class Planet extends GameObject {

	private static Image[] planetImg;
	private static int x;

	static {
		planetImg = new Image[2];
		planetImg[0] = new Image("img/parallax-space-big-planet.png", 300, 300, false, true);
		planetImg[1] = new Image("img/parallax-space-ring-planet.png", 500, 500, true, true);
	}

	public Planet(double X, double Y) {
		super(X, Y);
		Image img = planetImg[(x++) % 2];
		sprite = new ImageSprite(this, img);
		sprite.setZ(-1);
		sprite.getColorAdjust().setBrightness(0.15);
		addScript(new ConstantSpeedMove(0, 1)).addScript(new ColliderBox(img.getWidth(), img.getHeight()))
				.addScript(new AutoRemove(30));
	}

}
