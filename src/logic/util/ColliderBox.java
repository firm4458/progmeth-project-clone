package logic.util;

import javafx.geometry.*;
import logic.base.GameObject;
import logic.base.Script;

public class ColliderBox implements Script {
	
	private GameObject parent;
	private double width;
	private double height;
	
	public ColliderBox(double width,double height) {
		this.width = width;
		this.height = height;
	}
	
	public BoundingBox getBound() {
		return new BoundingBox(parent.getX(),parent.getY(),width,height);
	}

	@Override
	public void update() {
		
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		
	}
}
