package logic.util.scripts;

import drawing.Renderer;
import logic.base.BasicScript;
import logic.base.GameObject;

public class AutoRemove extends BasicScript<GameObject> {

	private int delay;

	public AutoRemove(int delay) {
		this.delay = delay;
	}

	public AutoRemove() {
		delay = 0;
	}

	@Override
	public void update() {
		if (delay != 0) {
			--delay;
			return;
		}
		if (!Renderer.getInstance().getCamera().isInCamera(parent)) {
			parent.destroy();
		}
	}

}
