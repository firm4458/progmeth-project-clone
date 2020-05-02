package logic.enemy;
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

public class Meteor extends Projectile{
	
	private static final Image img = new Image("img/Meteor.png", 29.2, 50, false, true);
	
	public Meteor(double X, double Y) {
		super(X, Y, 1, new ConstantSpeedMove(0, 3), new ColliderBox(29.2, 50),
				Player.playerGroup, 300);
		sprite = new ImageSprite(this, img );
	}
	
	@Override
	public void onDeath() {
		GameManager.getInstance().getCurrentScene().addGameObject(new ExplosionAnimation(getX(), getY()));
		GameManager.getInstance().setScore(GameManager.getInstance().getScore()+10);
		destroy();
	}

	@Override
	protected void actOn(Entity target) {
		target.getStatus().takeDamage(1);
		Renderer.getInstance().getCamera().setShake(true);
	}
	
}
