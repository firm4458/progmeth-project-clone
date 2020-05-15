package application.level;

import application.BaseLevelScene;
import logic.base.BasicScript;
import logic.base.GameInterruptException;
import logic.base.Script;
import logic.base.ScriptFactory;
import logic.base.ScriptNotFoundException;
import logic.enemy.AttackController;
import logic.enemy.AttackPickStrategy;
import logic.enemy.Enemy;
import logic.enemy.MinionSpawnAttack;
import logic.player.Player;
import logic.util.ResourceManager;
import logic.util.scripts.ColliderBox;
import logic.util.scripts.ConstantSpeedMove;
import logic.util.scripts.factory.ConstantSpeedMoveFactory;

public class Space3Boss extends Space3 {
	public Space3Boss() {
		super("space3.boss", ResourceManager.getSound("sound/boss3.mp3"));
		isBoss = true;
	}

	@Override
	public void init() {
		super.init();
		Enemy boss = new Enemy(150, -400, 50000, 5000, ResourceManager.getImage("boss.space3"));
		boss.setPoint(100000);
		boss.getSprite().setZ(80);
		boss.setOnDeathFunc(Enemy.BOSS_ON_DEATH);
		AttackController controller = new AttackController(AttackPickStrategy.RANDOM_NO_REPEAT_PICK);
		controller.getScripts().add(new MinionSpawnAttack(120, 350, 1200, new ConstantSpeedMoveFactory(0, 5)));
		controller.getScripts().add(new MinionSpawnAttack(120, 350, 4800, new ScriptFactory() {

			@Override
			public Script createScript() {
				return new BasicScript<Enemy>() {
					private double a;
					private double originalX = -1;
					private double direction = 1;

					@Override
					public void onAttach() {
						originalX = parent.getX();
						direction = Math.random() > 0.5 ? 1 : -1;
					}

					@Override
					public void update() {
						parent.translate(0, 2);
						parent.setX(originalX + direction * 280 * Math.sin(a));
						a += 0.02;
					}
				};
			}

		}));

		controller.getScripts().add(new MinionSpawnAttack(120, 350, 4800, new ScriptFactory() {

			@Override
			public Script createScript() {
				return new BasicScript<Enemy>() {
					@Override
					public void update() {
						parent.translate(0, 5);
						if (Math.random() < 0.01) {
							Enemy minion = MinionSpawnAttack.minionFactory.createGameObject();
							minion.addScript(new ConstantSpeedMove(5, 0));
							minion.setX(parent.getX());
							minion.setY(parent.getY());
							parent.getScene().addGameObject(minion);
							parent.getScene().addGameObject(minion,
									((BaseLevelScene) parent.getScene()).getEnemyGroup());
							minion = MinionSpawnAttack.minionFactory.createGameObject();
							minion.addScript(new ConstantSpeedMove(-5, 0));
							minion.setX(parent.getX());
							minion.setY(parent.getY());
							parent.getScene().addGameObject(minion);
							parent.getScene().addGameObject(minion,
									((BaseLevelScene) parent.getScene()).getEnemyGroup());
							parent.destroy();
						}
					}
				};
			}

		}));

		boss.addScript(new BasicScript<Enemy>() {
			@Override
			public void update() throws GameInterruptException {
				if (parent.getY() <= -parent.getSprite().getHeight() + 200) {
					parent.translate(0, 5);
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
		addGameObject(boss);
		addGameObject(boss, enemyGroup);
	}
}
