package logic.enemy;

import application.BaseLevelScene;
import application.GameManager;
import application.GameManager.GameEvent;
import application.GameManager.GameEventType;
import application.LevelResultScene;
import application.LevelResultScene.LevelResult;
import javafx.scene.image.Image;
import javafx.util.Pair;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.util.DataManager;
import logic.util.ResourceManager;
import logic.util.scripts.AutoDestroy;

public class Boss extends Enemy {
	private static Image img = ResourceManager.getImage("bossShip");
	private AttackController controller;
	private final Script movementScript = new Script() {
		GameObject parent;

		@Override
		public void update() throws GameInterruptException {
			if (parent.getY() <= -parent.getSprite().getHeight() + 200) {
				parent.translate(0, 0.2);
			} else {
				if (controller != null) {

				}
				parent.addScript(controller);
				parent.removeScript(this);
			}
		}

		@Override
		public GameObject getParent() {
			return parent;
		}

		@Override
		public void setParent(GameObject parent) throws IncompatibleScriptException {
			this.parent = parent;
		}
	};

	@Override
	public void onDeath() {
		DEFAULT_ON_DEATH.accept(this);
		GameManager manager = GameManager.getInstance();
		scene.addGameObject(
				new ExplosionAnimation(getX() + sprite.getWidth() / 2, getY() + sprite.getHeight() / 2, 10));
		System.out.println(getY() + sprite.getHeight() / 2);
		GameObject gameObj = new GameObject(0, 0);
		gameObj.addScript(new AutoDestroy(1000) {
			@Override
			public void onDestroy() {
				int totalScore = 0;
				if (DataManager.getInstance().contains("totalScore")) {
					totalScore = (int) DataManager.getInstance().getPesistentData("totalScore");
				}
				manager.signalEvent(new GameEvent(scene, GameEventType.WRITE_PERSISTENT_DATA,
						new Pair<String, Object>("totalScore", totalScore + ((BaseLevelScene) scene).getScore())));
				manager.signalEvent(new GameEvent(scene, GameEventType.SCENE_CHANGE,
						new LevelResultScene("result", LevelResult.WIN, ((BaseLevelScene) scene).getScore())));
			}
		});
		scene.addGameObject(gameObj);
	}

	public Boss(double X, double Y, int health, int damage, int point, Image img) {
		super(X, Y, health, damage, img);
		addScript(movementScript);
		setPoint(point);
		getSprite().setZ(50);
	}
}
