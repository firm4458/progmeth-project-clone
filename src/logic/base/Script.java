package logic.base;

public interface Script {
	public GameObject getParent();

	public void setParent(GameObject parent) throws IncompatibleScriptException;

	default public void onAttach() {
	}

	//method that use for update the data
	default public void update() throws GameInterruptException {
	};

	
	//method that use for early update the data
	default public void earlyUpdate() throws GameInterruptException {
	};

	//method that use for last update the data
	default public void lateUpdate() throws GameInterruptException {
	};

	default public void onDestroy() {
	}

}
