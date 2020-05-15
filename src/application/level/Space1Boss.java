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
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;

public class Space1Boss extends Space1 {

	public Space1Boss() {
		super("space1boss", ResourceManager.getSound("sound/boss.mp3"));
		isBoss = true;
	}

	@Override
	public void init() {
		super.init();
		Enemy boss = new Enemy(80, -600, 10000, 500, ResourceManager.getImage("bossShip"));
		boss.setPoint(50000);
		boss.getSprite().setZ(80);
		AttackController controller = new AttackController(AttackPickStrategy.RANDOM_NO_REPEAT_PICK);
		controller.getScripts().add(new RadialBulletAttack(220, 520, 6000, 10));
		controller.getScripts().add(new WaveBulletAttack(220, 520, 10000, 10));
		controller.getScripts().add(new RandomBulletAttack(220, 520, 4000, 10));
		boss.addScript(new BasicScript<Enemy>() {
			@Override
			public void update() throws GameInterruptException {
				if (parent.getY() <= -parent.getSprite().getHeight() + 200) {
					parent.translate(0, 0.2);
				} else {
					parent.addScript(controller);
					Player player = (Player) Player.playerGroup.getChildren().first();
					player.setSkillActive();
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
		boss.setOnDeathFunc(Enemy.BOSS_ON_DEATH);
		addGameObject(boss);
		addGameObject(boss, enemyGroup);
	}

}
