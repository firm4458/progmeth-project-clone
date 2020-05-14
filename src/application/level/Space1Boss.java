package application.level;

import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.ScriptNotFoundException;
import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.Enemy;
import logic.enemy.RadialBulletAttack;
import logic.enemy.RandomBulletAttack;
import logic.enemy.WaveBulletAttack;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;

public class Space1Boss extends Space1 {

	public Space1Boss() {
		super("space1boss", ResourceManager.getSound("sound/boss.mp3"));
	}

	@Override
	public void init() {
		super.init();
		Enemy boss = new Enemy(80, -600, 10000, 500, ResourceManager.getImage("boss.space1"));
		boss.setPoint(50000);
		AttackController controller = new AttackController(AttackPickStrategy.RANDOM_PICK);
		controller.getScripts().add(new RadialBulletAttack(250, 590, 6000));
		controller.getScripts().add(new WaveBulletAttack(250, 590, 10000));
		controller.getScripts().add(new RandomBulletAttack(250, 590, 4000));
		boss.addScript(new BasicScript<Enemy>() {
			@Override
			public void update() throws GameInterruptException {
				if (parent.getY() <= -parent.getSprite().getHeight() + 200) {
					parent.translate(0, 0.2);
				} else {
					parent.addScript(controller);
					parent.removeScript(this);
				}
			}
		});
		try {
			ColliderBox collider = boss.getScript(ColliderBox.class);
			collider.setRelativeX(100);
			collider.setWidth(collider.getWidth() - 200);
		} catch (ScriptNotFoundException e) {
			e.printStackTrace();
		}
		addGameObject(boss);
		addGameObject(boss, enemyGroup);
	}

}
