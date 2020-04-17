package logic.util;

import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptNotFoundException;

import java.util.ArrayList;

import javafx.geometry.BoundingBox;
import logic.util.ColliderBox;

public abstract class CollisionDetection implements Script {

	protected GameObject parent;
	ColliderBox colliderBox;
	GameObjectGroup targetGroup;

	public CollisionDetection(GameObjectGroup targetGroup) {
		this.targetGroup = targetGroup;
	}

	public abstract void onCollision(ArrayList<GameObject> targets);

	@Override
	public void update() {
		ArrayList<GameObject> targets = new ArrayList<GameObject>();
		for (GameObject target : targetGroup.getChildren()) {
			try {
				BoundingBox targetBound = target.getScript(ColliderBox.class).getBound();
				if (!target.isDestroyed() && colliderBox.getBound().intersects(targetBound)) {
					targets.add(target);
				}
			}catch(ScriptNotFoundException e) {
				System.out.println("Warning: GameObject with no ColliderBox Found in targetGroup");
			}
		}
		onCollision(targets);
	}

	@Override
	public GameObject getParent() {
		return (GameObject)parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			colliderBox = parent.getScript(ColliderBox.class); 
		}catch(ScriptNotFoundException e){
			throw new IncompatibleScriptException(getClass().toString(), "Must be attached to GameObject with ColliderBox");
		}
		this.parent = parent;
	}

	@Override
	public void onDestroy() {

	}

}
