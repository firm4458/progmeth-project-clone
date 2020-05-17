package logic.base;

public abstract class BasicScript<T extends GameObject> implements Script {
	protected T parent;

	@Override
	public GameObject getParent() {
		return parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		try {
			this.parent = (T) parent;
		} catch (ClassCastException ex) { //If can't cast the parent throws IncompatibleScriptException
			throw new IncompatibleScriptException(this.getClass().toString(), "incompatible");
		}
	}

}
