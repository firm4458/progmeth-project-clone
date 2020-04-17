package logic.player;

import java.util.ArrayList;

import application.GUI;
import drawing.Sprite;
import drawing.base.Renderable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;
import logic.util.ConstantSpeedMove;
import application.GUI;

public class Bullet extends GameObject {
	
	public Bullet(double X, double Y) {
		super(X,Y);
		sprite = new Sprite(this);
		addScript(new ConstantSpeedMove(0,-5)).
		addScript(new ColliderBox(50,50)).
		addScript(new AutoRemove()).
		addScript(new CollisionDetection(GUI.groupOfMeteors.getMeteors()) {
			
			@Override
			public void onCollision(ArrayList<GameObject> targets) {
				for(GameObject target: targets) {
					if(!target.isDestroyed()) {
						target.destroy();
						parent.destroy();
						return;
					}
				}
			}
		});
	}

}
