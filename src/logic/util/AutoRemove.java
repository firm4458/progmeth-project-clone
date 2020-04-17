package logic.util;

import drawing.Renderer;
import logic.base.GameObject;
import logic.base.Script;

public class AutoRemove implements Script {

	private GameObject parent;
	private boolean isInAtFirst;
	
	public AutoRemove(Boolean b) {
		isInAtFirst = b;
	}
	public AutoRemove() {
		isInAtFirst = true;
	}
	
	@Override
	public void update() {
		if(isInAtFirst && !Renderer.getInstance().getCamera().isInCamera(parent)) {
			parent.destroy();
		}else if(!isInAtFirst && Renderer.getInstance().getCamera().isInCamera(parent)) {
			isInAtFirst = true;
		}
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
