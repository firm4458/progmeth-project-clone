package logic.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import application.GUI;
import logic.base.GameObject;

public class GameObjectGroup implements Iterable<GameObject>{
	private final TreeSet<GameObject> children;
	private int framesBeforeUpdate;
	private int counter;

	public GameObjectGroup() {
		children = new TreeSet<GameObject>(GameObject.nameComparator);
		this.framesBeforeUpdate = 0;
		counter = 0;
	}

	public final void update() {
		if ((counter--) == 0) {
			counter = framesBeforeUpdate;
			updateAction();
		}
	}
	
	public void addGameObject(GameObject gameObj) throws DuplicateGameObjectException {
		boolean success = children.add(gameObj);
		if(!success) {
			throw new DuplicateGameObjectException(gameObj.getName());
		}
	}
	
	public GameObject getGameObject(String name) {
		GameObject result = null;
		for(GameObject gameObj : children) {
			if(gameObj.getName().equals(name)) {
				result = gameObj;
			}
		}
		return result;
	}
	
	public int size() {
		return children.size();
	}

	private void updateAction() {
		children.removeIf(gameObject -> gameObject.isDestroyed());
	}
	
	public TreeSet<GameObject> getChildren(){
		return children;
	}
	
	public void setFramesBeforeUpdate(int framesBeforeUpdate) {
		this.framesBeforeUpdate = framesBeforeUpdate;
	}

	@Override
	public Iterator<GameObject> iterator() {
		return children.iterator();
	}
}
