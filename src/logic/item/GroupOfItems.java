package logic.item;

import application.GUI;
import logic.base.GameObject;
import logic.util.GameObjectGroup;

public class GroupOfItems extends GameObject{
	private GameObjectGroup items;
	private ItemGenerator itemGenerator;
	
	public GroupOfItems() {
		super(0,0);
		items = GUI.sampleScene.createGroup();
		itemGenerator = new ItemGenerator(items);
		addScript(itemGenerator);
	}
	
	public GameObjectGroup getItems() {
		return items;
	}
}
