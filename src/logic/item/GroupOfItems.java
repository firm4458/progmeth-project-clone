package logic.item;

import application.GameManager;
import logic.base.GameObject;
import logic.util.group.GameObjectGroup;

public class GroupOfItems extends GameObject {
	private GameObjectGroup items;
	private ItemGenerator itemGenerator;

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
