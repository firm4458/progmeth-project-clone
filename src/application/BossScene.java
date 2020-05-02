package application;

import logic.enemy.Boss;

public class BossScene extends NormalLevelScene {

	@Override
	public void init() {
		bgmUrl = "sound/boss.mp3";
		super.init();
		groupOfMeteors.max_meteors = 40;
		addGameObject(new Boss(80,-600));
	}

}
