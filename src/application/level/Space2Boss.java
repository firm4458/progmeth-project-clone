package application.level;

import java.util.ArrayList;

import com.sun.javafx.geom.PickRay;

import application.BossScene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import logic.base.ScriptNotFoundException;
import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.Boss;
import logic.enemy.RadialBulletAttack;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;

public class Space2Boss extends Space2 {
	public Space2Boss() {
		super("space2.boss", ResourceManager.getSound("sound/boss.mp3"));
	}
	@Override
	public void init() {
		super.init();
		Boss boss = new Boss(80,-400,50000, 1000,50000,null,ResourceManager.getImage("boss.space2"));
		AttackController attackController = new AttackController(AttackPickStrategy.RANDOM_PICK);
		attackController.getScripts()
		.add(new RadialBulletAttack(boss.getX()+boss.getSprite().getWidth()/2 ,boss.getY() + boss.getSprite().getWidth()/2, 6000));
		try {
			ColliderBox collider = boss.getScript(ColliderBox.class);
			collider.setHeight(collider.getHeight()-100);
		} catch (ScriptNotFoundException e) {
			e.printStackTrace();
		}
		addGameObject(boss);
		addGameObject(boss,enemyGroup);
	}
}
