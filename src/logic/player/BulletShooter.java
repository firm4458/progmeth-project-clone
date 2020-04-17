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
			Bullet bullet = new Bullet(parent.getX()+15,parent.getY()-50);
			GUI.sampleScene.addGameObject(bullet);
			GUI.sampleScene.addGameObject(bullet,bullets);
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
}
