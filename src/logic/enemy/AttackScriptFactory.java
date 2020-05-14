package logic.enemy;

import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;

public abstract class AttackScriptFactory {
	public abstract AttackScript createScript();

	public abstract class AttackScript implements Script {

		protected Enemy parent;
		protected boolean isDone;

		@Override
		public GameObject getParent() {
			return parent;
		}

		@Override
		public void setParent(GameObject parent) throws IncompatibleScriptException {
			try {
				this.parent = (Enemy) parent;
			} catch (ClassCastException e) {
				throw new IncompatibleScriptException("AttackScript", "must be attached to Enemy");
			}
		}

		public boolean isDone() {
			return isDone;
		}

		protected void setDone(boolean isDone) {
			this.isDone = isDone;
		}

	}
}
