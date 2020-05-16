package logic.item;

import application.GameManager;
import logic.base.GameObject;
import logic.util.group.GameObjectGroup;

/**
 * This class extends from GameObject
 * items is a GameObjectGroup that use to contain item in it
 * itemGenerator is a Script to generate item
 * @author user
 *
 */

public class GroupOfItems extends GameObject {
	private GameObjectGroup items;
	private ItemGenerator itemGenerator;

	/**
	 * It will construct GameObject at position (0, 0) and create group at
	 * GameManager and addScript itemGenerator to generate item
	 */
	public GroupOfItems() {
		super(0, 0);
		items = GameManager.getInstance().getCurrentScene().createGroup();
		itemGenerator = new ItemGenerator(items);
		addScript(itemGenerator);
	}

	public GameObjectGroup getItems() {
		return items;
	}
}
