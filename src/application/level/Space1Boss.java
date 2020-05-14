package application.level;

import java.util.ArrayList;

import application.BossScene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.base.ScriptNotFoundException;
import logic.enemy.Boss;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;

public class Space1Boss extends Space1{
	
	public Space1Boss() {
		super("space1boss", ResourceManager.getSound("sound/boss.mp3"));
	}
	@Override
	public void init() {
		super.init();
		Boss boss = new Boss(80,-600,10000, 500,50000,null);
		try {
			ColliderBox collider = boss.getScript(ColliderBox.class);
			collider.setRelativeX(100);
			collider.setWidth(collider.getWidth()-200);
		} catch (ScriptNotFoundException e) {
			e.printStackTrace();
		}
		addGameObject(boss);
		addGameObject(boss,enemyGroup);
	}

}
