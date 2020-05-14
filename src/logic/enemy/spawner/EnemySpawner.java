package logic.enemy.spawner;

import java.util.TreeMap;

import application.GameManager;
import application.GameScene;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.enemy.Enemy;
import logic.enemy.EnemyFactory;
import logic.util.group.GameObjectGroup;

public class EnemySpawner implements Script {

	protected GameObject parent;
	protected EnemyFactory factory;
	protected SpawnStrategy spawnStrategy;
	protected GameScene currentScene;
	protected GameObjectGroup group;
	protected TreeMap<String, Object> data;

	public EnemySpawner(EnemyFactory factory, GameObjectGroup group, SpawnStrategy spawnStrategy) {
		this.factory = factory;
		currentScene = GameManager.getInstance().getCurrentScene();
		this.group = group;
		this.spawnStrategy = spawnStrategy;
		data = new TreeMap<String, Object>();
	}

	@Override
	public void lateUpdate() {
		while (spawnStrategy.checkConditionSpawn(factory, group, data)) {
			spawnStrategy.customizeFactory(factory, group, data);
			Enemy enemy = factory.createGameObject();
			currentScene.addGameObject(enemy);
			currentScene.addGameObject(enemy, group);
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

}
