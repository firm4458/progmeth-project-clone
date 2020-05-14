package logic.util.scripts.factory;

import logic.base.Script;
import logic.base.ScriptFactory;
import logic.util.scripts.AutoRemove;

public class AutoRemoveFactory extends ScriptFactory {

	private int delay;

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public AutoRemoveFactory() {
		this.delay = 0;
	}

	public AutoRemoveFactory(int delay) {
		this.delay = delay;
	}

	@Override
	public Script createScript() {
		return new AutoRemove(delay);
	}

}
