package application.level;

import java.util.ArrayList;

import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.ScriptNotFoundException;
import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.AttackScriptFactory;
import logic.enemy.DashAttack;
import logic.enemy.Enemy;
import logic.enemy.GlidingAttack;
import logic.enemy.RadialBulletAttack;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;

public class Space2Boss extends Space2 {
	public Space2Boss() {
		super("space2.boss", ResourceManager.getSound("sound/boss2.mp3"));
		isBoss = true;
	}

	@Override
	public void init() {
		super.init();
		Enemy boss = new Enemy(100, -400, 50000, 100, ResourceManager.getImage("boss.space2"));
		boss.setPoint(100000);
		boss.getSprite().setZ(80);
		boss.setOnDeathFunc(Enemy.BOSS_ON_DEATH);
		AttackController controller = new AttackController(new AttackPickStrategy() {

			@Override
			public AttackScriptFactory pick(ArrayList<AttackScriptFactory> factories, AttackController controller) {
				if (controller.getPickCount() == 0) {
					return factories.get(0);
				}
				return AttackPickStrategy.RANDOM_NO_REPEAT_PICK.pick(factories, controller);
			}
		});
		controller.getScripts().add(new DashAttack(-150));
		controller.getScripts().add(new RadialBulletAttack(200, 200, 1000, 20));
		controller.getScripts().add(new GlidingAttack(200, 200, 250, 5000, 20));
		/*
		 * controller.getScripts().add(new RadialBulletAttack(250, 590, 6000));
		 * controller.getScripts().add(new WaveBulletAttack(250, 590, 10000));
		 */

		boss.addScript(new BasicScript<Enemy>() {
			private long start = -1;

			@Override
			public void update() throws GameInterruptException {
				if (start == -1) {
					start = System.currentTimeMillis();
				}
				long now = System.currentTimeMillis();
				if (now - start > 13500) {
					Player player = (Player) Player.playerGroup.getChildren().first();
					player.setSkillActive();
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
