package logic.enemy;

import drawing.ImageSprite;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.util.IncompatibleScriptException;
import logic.util.ResourceManager;

public class Boss extends GameObject{
	public Boss(double X, double Y) {
		super(X,Y,"boss");
		setSprite(new ImageSprite(this,ResourceManager.getImage("img/boss.png",230*2, 300*2)));
		getSprite().setZ(50);
		addScript(new Script() {
			GameObject parent;
			@Override
			public void update() throws GameInterruptException {
				if(parent.getY()<=-400) {
					parent.translate(0,2.0/9.0);
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

			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
