package logic.enemy;

import java.util.ArrayList;

import application.GameManager;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.Entity;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.player.Player;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;

public class Enemy extends Entity {

	protected int point;
	
	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public void onDeath() {
		GameManager.getInstance().getCurrentScene().addGameObject(new ExplosionAnimation(getX(), getY()));
		GameManager.getInstance().setScore(GameManager.getInstance().getScore() + getPoint());
		destroy();
	}

	public Enemy(double X, double Y, int health, int damage, Script motionScript, Script attackScript, Image image) {
		super(X, Y, health);
		getStatus().setDamage(damage);
		setSprite(new ImageSprite(this, image));
		if (motionScript != null) {
			addScript(motionScript);
		}
		if (attackScript != null) {
			addScript(attackScript);
		}
		addScript(new ColliderBox(image.getWidth(), image.getHeight()));
		addScript(new CollisionDetection(Player.playerGroup) {
			
			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				onHitPlayer(targets);
			}
		});
	}
	
	protected void onHitPlayer(ArrayList<GameObject> targets) {
		for(GameObject gameObj : targets) {
			if(gameObj.isDestroyed()) {
				continue;
			}
			Player player = (Player)gameObj;
			player.getStatus().takeDamage(getStatus().getDamage());
		}
	}

}
