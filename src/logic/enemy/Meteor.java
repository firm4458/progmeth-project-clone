package logic.enemy;
import java.util.ArrayList;

import application.GameManager;
import application.NormalLevelScene;
import drawing.Camera;
import drawing.ImageSprite;
import drawing.Renderer;
import drawing.Sprite;
import drawing.base.Renderable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.Projectile;
import logic.base.Entity;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptNotFoundException;
import logic.player.Player;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.ConstantSpeedMove;
import logic.util.IncompatibleScriptException;
import logic.util.ResourceManager;

public class Meteor extends Enemy{
	
	private static final Image img = ResourceManager.getImage("meteor");
	
	public Meteor(double X, double Y) {
		super(X, Y, 10, 1, new ConstantSpeedMove(0, 3), null, img);
		addScript(new AutoRemove(60));
		setPoint(10);
	}
	
	@Override
	protected void onHitPlayer(ArrayList<GameObject> targets) {
		super.onHitPlayer(targets);
		destroy();
	}
	
}
