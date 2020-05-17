package logic.base;

public interface Updatable extends Destroyable {
	//method that use for update the data
	default public void update() throws GameInterruptException {
	};

	//method that use for early update the data
	default public void earlyUpdate() throws GameInterruptException {
	};

	
	//method that use for last update the data
	default public void lateUpdate() throws GameInterruptException {
	};

	@Override
	//this method always return false
	default public boolean isDestroyed() {
		return false;
	}

	default public void onDestroy() {
	}

	@Override
	default public void destroy() {
	}
}
