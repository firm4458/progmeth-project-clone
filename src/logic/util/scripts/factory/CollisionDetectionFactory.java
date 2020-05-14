package logic.util.scripts.factory;

import java.util.ArrayList;
import java.util.function.Consumer;

import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.group.GameObjectGroup;
import logic.util.scripts.CollisionDetection;

public class CollisionDetectionFactory extends ScriptFactory {

	GameObjectGroup targetGroup;
	private Consumer<ArrayList<GameObject>> onCollisionFunc = (targets) -> {
		System.out.println("Warning: no onCollisionFunc");
	};

	public GameObjectGroup getTargetGroup() {
		return targetGroup;
	}

	public void setTargetGroup(GameObjectGroup targetGroup) {
		this.targetGroup = targetGroup;
	}

	public Consumer<ArrayList<GameObject>> getOnCollisionFunc() {
		return onCollisionFunc;
	}

	public void setOnCollisionFunc(Consumer<ArrayList<GameObject>> onCollisionFunc) {
		this.onCollisionFunc = onCollisionFunc;
	}

	@Override
	public Script createScript() {
		return new CollisionDetection(targetGroup) {
			@Override
			public void onCollision(ArrayList<GameObject> targets) {
				onCollisionFunc.accept(targets);
			}
		};
	}

}
