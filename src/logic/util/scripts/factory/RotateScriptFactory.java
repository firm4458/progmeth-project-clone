package logic.util.scripts.factory;

import drawing.ImageSprite;
import logic.base.BasicScript;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptFactory;

public class RotateScriptFactory extends ScriptFactory {

	private double rot;

	public double getRot() {
		return rot;
	}

	public void setRot(double rot) {
		this.rot = rot;
	}

	@Override
	public Script createScript() {
		return new RotateScript(rot);
	}

	private class RotateScript extends BasicScript<GameObject> {
		private double rot;

		public RotateScript(double rot) {
			this.rot = rot;
		}

		@Override
		public void update() {
			ImageSprite sprite = (ImageSprite) parent.getSprite();
			sprite.setRotate(rot);
		}
	}

}
