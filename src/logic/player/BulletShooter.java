package logic.player;

import java.util.ArrayList;

import application.GUI;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.GameObjectGroup;

public class BulletShooter implements Script {
	
	GameObjectGroup bullets = GUI.sampleScene.createGroup();
	private static final int COOLDOWN=10;
	private static final int MAX_BULLET=10;
	private int cooldown=COOLDOWN;
	GameObject parent;

	@Override
	public void update() {
		
		if(cooldown==0 && MAX_BULLET > bullets.size()) {
			if(parent.getClass() == Player.class) {
				addBullet(parent.getX()+30, parent.getY()-50);
				if(((Player)parent).getUpgradeAmmo()) {
					addBullet(parent.getX()+10, parent.getY()-50);
					addBullet(parent.getX()+50, parent.getY()-50);
				}
			}
		}
			
		--cooldown;
		if(cooldown<0) {
			cooldown = COOLDOWN;
		}
		
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		for(GameObject bullet : bullets.getChildren()) {
			bullet.destroy();
		}
	}
	
	public void addBullet(double X, double Y) {
		Bullet bullet = new Bullet(X, Y);
		GUI.sampleScene.addGameObject(bullet);
		GUI.sampleScene.addGameObject(bullet, bullets);
		
	}
}
