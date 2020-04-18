package logic.util;

import javafx.geometry.*;
import logic.base.GameObject;
import logic.base.Script;

public class ColliderBox implements Script {
	
	private GameObject parent;
	private double width;
	private double height;
	private double relativeX;
	private double relativeY;
	
	public ColliderBox(double width,double height) {
		this.width = width;
		this.height = height;
	}
	public ColliderBox(double relativeX, double relativeY, double width,double height) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.width = width;
		this.height = height;
	}
	
	public BoundingBox getBound() {
		return new BoundingBox(parent.getX()+relativeX,parent.getY()+relativeY,width,height);
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
