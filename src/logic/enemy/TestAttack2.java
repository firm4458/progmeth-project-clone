package logic.enemy;

import javafx.scene.input.KeyCode;
import logic.base.GameInterruptException;
import logic.enemy.AttackScriptFactory.AttackScript;
import logic.enemy.TestAttack1.TestAttack1Script;
import logic.util.InputUtil;

public class TestAttack2 extends AttackScriptFactory{
	@Override
	public AttackScript createScript() {
		return new TestAttack2Script();
	}
	
	public class TestAttack2Script extends AttackScriptFactory.AttackScript {

		@Override
		public void update() throws GameInterruptException {
			if(!isDone() &&!InputUtil.isKeyPressed(KeyCode.Q)) {
				System.out.println("test2");
			}else {
				setDone(true);
			}
		}
		
	}
}
