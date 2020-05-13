package logic.enemy;

import java.util.ArrayList;

import application.GameManager;
import logic.base.GameObject;
import logic.base.ScriptFactory;
import logic.util.scripts.factory.AutoRemoveFactory;
import logic.util.scripts.factory.ConstantSpeedMoveFactory;

public class MeteorFactory extends EnemyFactory {
	public MeteorFactory() {
		super();
		setHealth(10);
		setDamage(1);
		setImage(Meteor.IMG);
		ArrayList<ScriptFactory> scriptFactories = getScriptFactories();
		scriptFactories.add(new ConstantSpeedMoveFactory(0,Meteor.SPEED));
		scriptFactories.add(new AutoRemoveFactory());
	}
	@Override
	public Enemy createGameObject() {
		Enemy enemy = new Enemy(X, Y, health, damage, null, null, image) {
			@Override
			protected void onHitPlayer(ArrayList<GameObject> targets) {
				super.onHitPlayer(targets);
				GameManager.getInstance().getCurrentScene().addGameObject(new ExplosionAnimation(getX(), getY()));
				destroy();
			}
		};
		scriptFactories.forEach(factory->enemy.addScript(factory.createScript()));
		return enemy;
	}
}
