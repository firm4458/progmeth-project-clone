package logic.base;

public interface Script extends Updatable {
	public GameObject getParent();
	public void setParent(GameObject parent) throws IncompatibleScriptException;
	default public void onAttach() {}
}
