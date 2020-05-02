package logic;

import application.GUI;
import application.GameManager;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.IncompatibleScriptException;

public class PlanetSpawner implements Script {
	
	Planet planet;
	GameObject parent;
	private int cooldown;
	private int COOLDOWN=10;
	
	public PlanetSpawner() {
		planet = new Planet(Math.random()*GameManager.NATIVE_WIDTH,0);
		planet.translate(0, -planet.getSprite().getHeight());
		GameManager.getInstance().getCurrentScene().addGameObject(planet);
		cooldown = COOLDOWN;
	}

	@Override
	public void update() {
		//System.out.println((new StringBuilder()).append(planet.getX()).append(' ').append(planet.getY()));
		if(planet.isDestroyed()) {
			if((cooldown--)<=0) {
				planet = new Planet(Math.random()*GameManager.NATIVE_WIDTH,-500);
				GameManager.getInstance().getCurrentScene().addGameObject(planet);
				cooldown = COOLDOWN;
			}
		}
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
		// TODO Auto-generated method stub
		
	}
	
}
