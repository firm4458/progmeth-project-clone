package logic.util.scripts;

import logic.base.BasicScript;
import logic.base.GameObject;

public class AutoDestroy extends BasicScript<GameObject> {
	private long duration;
	private long start;

	public AutoDestroy(long duration) {
		this.duration = duration;
		start = System.currentTimeMillis();
	}

	@Override
	public void update() {
		long now = System.currentTimeMillis();
		if (now - start > duration) {
			parent.destroy();
		}
	}
}
