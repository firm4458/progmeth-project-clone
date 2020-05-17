package logic.player;

import drawing.ImageSprite;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import logic.base.Dio;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.ScriptNotFoundException;
import logic.util.ResourceManager;

public class TheWorld extends PlayerSkill {

	private MediaPlayer zawarudo;
	private long start;
	private long duration = 10000;
	private TimeSphere timeSphere = null;

	public TheWorld(Player parent) {
		super(KeyCode.X, 5000);
	}

	/**
	 *set scene that player live to freeze
	 *play zawarudo sound
	 *set start to current time
	 */
	@Override
	protected void startSkill() {
		parent.getScene().setFreezing(true);
		zawarudo = new MediaPlayer(ResourceManager.getSound("sound/zawarudo.mp3"));
		zawarudo.play();
		start = System.currentTimeMillis();
	}

	/**
	 * update skill
	 */
	@Override
	protected void skillUpdate() {
		long now = System.currentTimeMillis();
		timeDuration = duration - (now - start);
		if (now - start > duration) {
			try {
				timeSphere.getScript(ExpansionScript.class).expanding = false;
			} catch (ScriptNotFoundException e) {
				e.printStackTrace();
			}
			timeSphere = null;
			parent.getScene().setFreezing(false);
			skillDone();
		} else if (now - start > 1000 && timeSphere == null) {
			timeSphere = new TimeSphere(300, 300);
			parent.getScene().addGameObject(timeSphere);
		}
	}

	/**
	 * GameObject that has shield image
	 * add expansion script
	 * @author user
	 *
	 */
	private class TimeSphere extends GameObject implements Dio {
		public TimeSphere(double X, double Y) {
			super(X, Y);
			sprite = new ImageSprite(this, ResourceManager.getImage("shield"));
			sprite.setZ(100);
			((ImageSprite) sprite).setScale(0);
			addScript(new ExpansionScript(X, Y));
		}
	}

	/**
	 * extends the picture of this sprite until scale is more than maxScale
	 * Then destory itself
	 */
	private class ExpansionScript implements Script {
		double scale = 0;
		double maxScale = 2.0;
		double originX;
		double originY;
		GameObject parent;
		boolean expanding = true;
		double step = 0.05;

		public ExpansionScript(double originX, double originY) {
			super();
			this.originX = originX;
			this.originY = originY;
		}


		@Override
		public void update() throws GameInterruptException {

			if ((expanding && scale < maxScale) || (!expanding && scale >= 0)) {
				ImageSprite sprite = (ImageSprite) parent.getSprite();
				scale += expanding ? step : -step * 5;
				sprite.setScale(scale);
				parent.setX(originX - sprite.getWidth() / 2 * scale);
				parent.setY(originY - sprite.getHeight() / 2 * scale);
			} else if (!expanding && scale <= 0) {
				parent.destroy();
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

}
