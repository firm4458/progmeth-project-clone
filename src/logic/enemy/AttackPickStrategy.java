package logic.enemy;

import java.util.ArrayList;

public interface AttackPickStrategy {
	public AttackScriptFactory pick(ArrayList<AttackScriptFactory> factories, AttackController controller);
	public static final AttackPickStrategy RANDOM_PICK = new AttackPickStrategy() {
		@Override
		public AttackScriptFactory pick(ArrayList<AttackScriptFactory> factories, AttackController controller) {
			int rand = (int)(Math.random()*factories.size());
			return factories.get(rand);
		};
		
	};
}
