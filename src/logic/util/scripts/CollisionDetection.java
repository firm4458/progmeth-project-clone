package logic.util.scripts;

import java.util.ArrayList;

import javafx.geometry.BoundingBox;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptNotFoundException;
import logic.util.group.GameObjectGroup;

public abstract class CollisionDetection implements Script {

	protected GameObject parent;
	ColliderBox colliderBox;
	GameObjectGroup targetGroup;

	public CollisionDetection(GameObjectGroup targetGroup) {
		this.targetGroup = targetGroup;
	}

	public abstract void onCollision(ArrayList<GameObject> targets) throws GameInterruptException;

	@Override
	public void update() throws GameInterruptException {
		ArrayList<GameObject> targets = new ArrayList<GameObject>();
		for (GameObject target : targetGroup.getChildren()) {
			try {
				BoundingBox targetBound = target.getScript(ColliderBox.class).getBound();
				if (!target.isDestroyed() && colliderBox.getBound().intersects(targetBound)) {
					targets.add(target);
				}
			} catch (ScriptNotFoundException e) {
				System.out.println("Warning: GameObject with no ColliderBox Found in targetGroup");
			}
		}
		if (targets.size() != 0) {
			onCollision(targets);
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			colliderBox = parent.getScript(ColliderBox.class);
		} catch (ScriptNotFoundException e) {
			throw new IncompatibleScriptException(getClass().toString(),
					"Must be attached to GameObject with ColliderBox");
		}
		this.parent = parent;
	}

	@Override
	public void onDestroy() {

	}

}
