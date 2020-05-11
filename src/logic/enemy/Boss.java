package logic.enemy;

import application.LevelSelectScene;
import drawing.ImageSprite;
import javafx.scene.image.Image;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.util.IncompatibleScriptException;
import logic.util.ResourceManager;

public class Boss extends Enemy {
	private static Image img = ResourceManager.getImage("bossShip");
	private final Script movementScript = new Script() {
		GameObject parent;

		@Override
		public void update() throws GameInterruptException {
			if (parent.getY() <= -400) {
				parent.translate(0, 0.2);
			} else {
				AttackController controller = new AttackController(AttackPickStrategy.RANDOM_PICK);
				controller.getScripts().add(new RadialBulletAttack(parent.getX() + parent.getSprite().getWidth() / 2,
						10, 6000));
				controller.getScripts().add(new WaveBulletAttack(parent.getX() + parent.getSprite().getWidth() / 2,
						10, 10000));
				controller.getScripts().add(new RandomBulletAttack(parent.getX() + parent.getSprite().getWidth() / 2,
						10, 4000));
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
	public void onDeath() throws SceneChangeInterruptException {
		throw new SceneChangeInterruptException(new LevelSelectScene()); 
	}

	public Boss(double X, double Y, Script motionScript, AttackController controller) {
		super(X, Y, 100000, 50000, motionScript, controller, img);
		addScript(movementScript);
		getSprite().setZ(50);
	}
}
