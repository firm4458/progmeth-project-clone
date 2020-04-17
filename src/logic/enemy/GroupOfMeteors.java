package logic.enemy;

import java.util.ArrayList;

import application.GUI;
import logic.base.GameObject;
import logic.util.GameObjectGroup;

public class GroupOfMeteors extends GameObject {
	static int MAX_METEORS = 10;
	private GameObjectGroup meteors;
	private MeteorGenerator meteorGenerator;
	
	public GroupOfMeteors() {
		super(0,0);
		meteors = GUI.sampleScene.createGroup();
		meteorGenerator = new MeteorGenerator(meteors);
		addScript(meteorGenerator);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	public GameObjectGroup getMeteors(){
		return meteors;
	}
	
}
