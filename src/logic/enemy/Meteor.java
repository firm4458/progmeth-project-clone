package logic.enemy;
import drawing.Sprite;
import javafx.geometry.BoundingBox;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.ConstantSpeedMove;

public class Meteor extends GameObject{
	
	public Meteor(double X, double Y) {
		super(X,Y);
		sprite = new Sprite(this);
		GameObject gameObj = this;
		addScript(new ConstantSpeedMove(0,3)).
		addScript(new ColliderBox(50,50)).
		addScript(new AutoRemove(20));
	}
}
