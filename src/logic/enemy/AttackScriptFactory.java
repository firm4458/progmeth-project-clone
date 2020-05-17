package logic.enemy;

import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptFactory;

public abstract class AttackScriptFactory extends ScriptFactory {
	
	/**
	 * use to construct script
	 */
	@Override
	public abstract AttackScript createScript();

	public abstract class AttackScript extends BasicScript<Enemy> {
		protected boolean isDone;

		public boolean isDone() {
			return isDone;
		}

		protected void setDone(boolean isDone) {
			this.isDone = isDone;
		}

	}
}
