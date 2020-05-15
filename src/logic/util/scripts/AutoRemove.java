package logic.util.scripts;

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
		if (!parent.getScene().getSimpleCamera().isInCamera(parent)) {
			parent.destroy();
		}
	}

}
