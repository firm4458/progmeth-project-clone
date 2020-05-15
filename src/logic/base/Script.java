package logic.base;

public interface Script {
	public GameObject getParent();

	public void setParent(GameObject parent) throws IncompatibleScriptException;

	default public void onAttach() {
	}

	default public void update() throws GameInterruptException {
	};

	default public void earlyUpdate() throws GameInterruptException {
	};

	default public void lateUpdate() throws GameInterruptException {
	};

	default public void onDestroy() {
	}

}
