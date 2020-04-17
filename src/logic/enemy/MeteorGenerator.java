package logic.enemy;

import java.util.ArrayList;

import application.GUI;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.GameObjectGroup;

public class MeteorGenerator implements Script {

	private GameObject parent;
	private GameObjectGroup meteors;

	public MeteorGenerator(GameObjectGroup meteors) {
		this.meteors = meteors;
	}

	@Override
	public void update() {
		
		int added = 0;
		while(meteors.size()+added < GroupOfMeteors.MAX_METEORS) {
			Meteor meteor = new Meteor(600*Math.random() - 20,-200*Math.random()-50);
			GUI.sampleScene.addGameObject(meteor);
			GUI.sampleScene.addGameObject(meteor,meteors);
			++added;
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
		for (GameObject meteor : meteors.getChildren()) {
			meteor.destroy();
		}
	}

}
