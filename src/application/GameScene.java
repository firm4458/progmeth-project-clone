package application;

import java.util.ArrayList;

import javafx.util.Pair;
import logic.base.Destroyable;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.util.GameObjectGroup;

public abstract class GameScene implements Destroyable {

	protected GameObjectGroup allObj;
	protected ArrayList<Pair<GameObject, GameObjectGroup>> addBuffer;

	protected ArrayList<GameObjectGroup> groups;

	protected boolean isDestroyed=false;

	private String name;

	public abstract void init();

	public GameScene() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
	}

	public void update() throws GameInterruptException {

		for (GameObject gameObj : allObj.getChildren()) {
			gameObj.update();
		}
		
		if(isDestroyed()) {
			System.out.println("GG");
		}

		allObj.update();
		for (GameObjectGroup group : groups) {
			group.update();
		}

		for (var p : addBuffer) {
			if(isDestroyed) {
				System.out.println("AAAA");
			}
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

	@Override
	public void destroy() {
		isDestroyed = true;
		for (GameObject gameObj : allObj.getChildren()) {
			gameObj.destroy();
		}
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
