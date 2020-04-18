package logic.util;

import drawing.Renderer;
import logic.base.GameObject;
import logic.base.Script;

public class AutoRemove implements Script {

	private GameObject parent;
	private int delay;
	
	public AutoRemove(int delay) {
		this.delay = delay;
	}
	public AutoRemove() {
		delay=0;
	}
	
	@Override
	public void update() {
		if(delay!=0) {
			--delay;
			return;
		}
		if(!Renderer.getInstance().getCamera().isInCamera(parent)) {
			parent.destroy();
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
