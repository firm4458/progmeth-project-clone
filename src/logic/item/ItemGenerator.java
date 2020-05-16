package logic.item;

import application.GameManager;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.util.group.GameObjectGroup;

/**
 * <h1> ItemGenerator </h1>
 * This class implements from Script and usually generate item to items GameObjectGroup
 * @author user
 *
 */

public class ItemGenerator implements Script {
	private GameObject parent;
	private GameObjectGroup items;
	private int cooldown = 0;

	/**
	 * Contruct
	 * @param items This is GameObjectGroup that contain item
	 */
	public ItemGenerator(GameObjectGroup items) {
		this.items = items;
	}

	/**
	 * this method generate item every 100/60 second by construct Item and
	 * add it to GameManager AllObjectGroup and items GameObjectGroup
	 */
	@Override
	public void update() {
		if (cooldown == 0) {
			cooldown = 100;
			Item item = new Item();
			GameManager.getInstance().getCurrentScene().addGameObject(item);
			GameManager.getInstance().getCurrentScene().addGameObject(item, items);
		}

		cooldown--;
	}

	@Override
	public GameObject getParent() {
		return parent;
	}

	@Override
	public void setParent(GameObject parent) throws IncompatibleScriptException {
		this.parent = parent;
	}

	@Override
	public void onDestroy() {
		for (GameObject item : items.getChildren()) {
			item.destroy();
		}
	}

}
