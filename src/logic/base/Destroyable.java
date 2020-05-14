package logic.base;

public interface Destroyable {
	// destroy that object by setting active to false
	public void destroy();

	// check if the object is active
	public boolean isDestroyed();
}
