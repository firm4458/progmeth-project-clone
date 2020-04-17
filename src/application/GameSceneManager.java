package application;

import java.util.ArrayList;

import javafx.util.Pair;
import logic.base.GameObject;
import logic.util.GameObjectGroup;

public class GameSceneManager {

	private GameObjectGroup allObj;
	private ArrayList<Pair<GameObject, GameObjectGroup>> addBuffer;

	private ArrayList<GameObjectGroup> groups;

	public GameSceneManager() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
	}

	public void update() {

		for (GameObject gameObj : allObj.getChildren()) {
			gameObj.update();
		}

		allObj.update();
		for (GameObjectGroup group : groups) {
			group.update();
		}

		for (var p : addBuffer) {
			p.getValue().getChildren().add(p.getKey());
		}
		
		addBuffer.clear();

	}

	public void addGameObject(GameObject gameObj) {
		addBuffer.add(new Pair<GameObject, GameObjectGroup>(gameObj, allObj));
	}

	public void addGameObject(GameObject gameObj, GameObjectGroup group) {
		addBuffer.add(new Pair<GameObject, GameObjectGroup>(gameObj, group));
	}

	public GameObjectGroup createGroup() {
		GameObjectGroup group = new GameObjectGroup();
		groups.add(group);
		return group;
	}
}
