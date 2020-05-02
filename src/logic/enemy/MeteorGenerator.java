package logic.enemy;

import java.util.ArrayList;

import application.GUI;
import application.GameManager;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;

public class MeteorGenerator implements Script {

	private GroupOfMeteors parent;
	private GameObjectGroup meteors;

	public MeteorGenerator(GameObjectGroup meteors) {
		this.meteors = meteors;
	}

	@Override
	public void update() {
		
		int added = 0;
		while(meteors.size()+added < parent.max_meteors) {
			Meteor meteor = new Meteor(GameManager.NATIVE_WIDTH*Math.random() - 20,-200*Math.random()-50);
			GameManager.getInstance().getCurrentScene().addGameObject(meteor);
			GameManager.getInstance().getCurrentScene().addGameObject(meteor,meteors);
			++added;
		}
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (GroupOfMeteors)parent;
		} catch (ClassCastException e) {
			throw new IncompatibleScriptException("meteor generator", "must be attached to GroupOfMeteor");
		}
	}

	@Override
	public void onDestroy() {
		for (GameObject meteor : meteors.getChildren()) {
			meteor.destroy();
		}
	}

}
