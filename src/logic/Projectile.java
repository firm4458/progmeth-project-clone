package logic;

import java.util.ArrayList;

import logic.base.Entity;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.AutoRemove;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;

public abstract class Projectile extends Entity {
	
	private int hitCount = 0;
	private static final int MAX_HIT_COUNT=1;
	
	protected abstract void actOn(Entity target);

	protected void onHit(ArrayList<GameObject> targets) {
		for(GameObject target: targets) {
			if(isDestroyed()) {
				break;
			}
			if(target.isDestroyed()) {
				continue;
			}
			if(hitCount==0) {
				break;
			}
			try {
				Entity entity = (Entity)target;
				actOn(entity);
				hitCount--;
			}catch(ClassCastException e) {
				e.printStackTrace();
				System.err.println(target.toString()+" is not an Entity");
			}
		}
		if(hitCount<=0) {
			destroy();
		}
	}

	public Projectile(double X, double Y,int health, Script motionScript, ColliderBox colliderBox, GameObjectGroup targetGroup, boolean autoRemove) {
		this(X, Y, health,motionScript, colliderBox, targetGroup, autoRemove?20:-1);
	}
	public Projectile(double X, double Y,int health, Script motionScript,ColliderBox colliderBox, GameObjectGroup targetGroup, int delayRemove) {
		super(X, Y, health);
		hitCount = MAX_HIT_COUNT;
		addScript(motionScript);
		addScript(colliderBox);
		addScript(new CollisionDetection(targetGroup) {
			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				onHit(targets);
			}
		});
		if(delayRemove>=0) {
			addScript(new AutoRemove(delayRemove));
		}
	}

}
