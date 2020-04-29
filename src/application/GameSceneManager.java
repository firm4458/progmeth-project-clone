package application;

import java.util.ArrayList;

import javafx.util.Pair;
import logic.base.Destroyable;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.util.GameObjectGroup;

public class GameSceneManager implements Destroyable{

	private GameObjectGroup allObj;
	private ArrayList<Pair<GameObject, GameObjectGroup>> addBuffer;

	private ArrayList<GameObjectGroup> groups;
	
	private boolean isDestroyed;

	public GameSceneManager() {
		allObj = new GameObjectGroup();
		groups = new ArrayList<GameObjectGroup>();
		addBuffer = new ArrayList<Pair<GameObject, GameObjectGroup>>();
	}

	public void update() {
		try {
			for (GameObject gameObj : allObj.getChildren()) {
				gameObj.update();
			}
		}catch(SceneChangeInterruptException e) {
			GameManager.getInstance().setScene(e.getScene());
			return;
		} catch (GameInterruptException e) {
			e.printStackTrace();
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

	@Override
	public void destroy() {
		isDestroyed = true;
		for(GameObject gameObj : allObj.getChildren()) {
			gameObj.destroy();
		}
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
