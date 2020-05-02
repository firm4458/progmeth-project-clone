package logic.enemy;

import java.util.ArrayList;

import application.GUI;
import application.GameManager;
import logic.base.GameObject;
import logic.util.GameObjectGroup;

public class GroupOfMeteors extends GameObject {
	public int max_meteors = 25;
	private GameObjectGroup meteors;
	private MeteorGenerator meteorGenerator;
	
	public GroupOfMeteors() {
		super(0,0);
		meteors = GameManager.getInstance().getCurrentScene().createGroup();
		meteorGenerator = new MeteorGenerator(meteors);
		addScript(meteorGenerator);
	}
	
	public GameObjectGroup getMeteors(){
		return meteors;
	}
	
}
