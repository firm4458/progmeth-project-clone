package application;

import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.Boss;
import logic.enemy.GroupOfMeteors;
import logic.enemy.TestAttack1;
import logic.enemy.TestAttack2;

public class BossScene extends NormalLevelScene {

	@Override
	public void init() {
		bgmUrl = "sound/boss.mp3";
		super.init();
		groupOfMeteors.max_meteors = 40;
		
		Boss boss = new Boss(80,-600,null,null);
		addGameObject(boss);
		addGameObject(boss,groupOfMeteors.getMeteors());
	}

}
