package logic.base;

public abstract class GameObjectFactory<T extends GameObject> {
	public abstract T createGameObject();
}
