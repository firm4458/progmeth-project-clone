package logic.base;

import logic.util.IncompatibleScriptException;

public interface Script {
	default public void earlyUpdate()  throws GameInterruptException{}
	public void update() throws GameInterruptException;
	default public void lateUpdate()  throws GameInterruptException{}
	default public void onDestroy(){};
	public GameObject getParent();
	public void setParent(GameObject parent) throws IncompatibleScriptException;
}
