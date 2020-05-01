package logic.enemy;
import drawing.ImageSprite;
import drawing.Sprite;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.ConstantSpeedMove;

public class Meteor extends GameObject{
	
	public Meteor(double X, double Y) {
		super(X,Y);
		sprite = new ImageSprite(this, new Image("img/Meteor.png", 50, 50, true, true));
		GameObject gameObj = this;
		addScript(new ConstantSpeedMove(0,3)).
		addScript(new ColliderBox(50,50)).
		addScript(new AutoRemove(20));
	}
}
