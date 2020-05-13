package logic.base;

public interface Updatable extends Destroyable {
	default public void update() throws GameInterruptException{};
	default public void earlyUpdate() throws GameInterruptException {};
	default public void lateUpdate() throws GameInterruptException{};
	default public boolean isDestroyed() {return false;} 
	default public void onDestroy() {}
	default public void destroy() {}
}
