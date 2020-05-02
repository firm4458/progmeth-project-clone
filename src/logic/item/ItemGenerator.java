package logic.item;

import application.GUI;
import application.GameManager;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.GameObjectGroup;
import logic.util.IncompatibleScriptException;

public class ItemGenerator implements Script {
	private GameObject parent;
	private GameObjectGroup items;
	private int cooldown = 0;
	
	public ItemGenerator(GameObjectGroup items) {
		this.items = items;
	}
	
	@Override
	public void update() {
		if(cooldown == 0) {
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
