package logic.util;

import java.util.ArrayList;

import application.GUI;
import logic.base.GameObject;

public class GameObjectGroup {
	private final ArrayList<GameObject> children;
	private int framesBeforeUpdate;
	private int counter;

	public GameObjectGroup() {
		children = new ArrayList<GameObject>();
		this.framesBeforeUpdate = 0;
		counter = 0;
	}

	public final void update() {
		if ((counter--) == 0) {
			counter = framesBeforeUpdate;
			updateAction();
		}
	}
	
	public int size() {
		return children.size();
	}

	private void updateAction() {
		children.removeIf(gameObject -> gameObject.isDestroyed());
	}
	
	public ArrayList<GameObject> getChildren(){
		return children;
	}
	
	public void setFramesBeforeUpdate(int framesBeforeUpdate) {
		this.framesBeforeUpdate = framesBeforeUpdate;
	}
}
