package logic.enemy.spawner;

import java.util.Map;

import logic.enemy.EnemyFactory;
import logic.util.group.GameObjectGroup;

public interface SpawnStrategy {
	public boolean checkConditionSpawn(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data);

	public void customizeFactory(EnemyFactory factory, GameObjectGroup group, Map<String, Object> data);
}
