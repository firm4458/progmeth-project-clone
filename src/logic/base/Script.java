package logic.base;

import logic.util.IncompatibleScriptException;

public interface Script {
	public void update();
	public GameObject getParent();
	public void setParent(GameObject parent) throws IncompatibleScriptException;
	public void onDestroy();
}
