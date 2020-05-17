package logic.player;

import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import logic.base.BasicScript;
import logic.base.EntityStatus;
import logic.base.ScriptNotFoundException;
import logic.util.ResourceManager;

public class Dash extends PlayerSkill {

	private static long cooldown = 500;
	private static long duration = 50;
	private long start;
	private double normalSpeed;
	private PlayerController playerController;
	private EntityStatus status;
	private MediaPlayer mediaPlayer;

	public Dash(Player parent) {
		super(KeyCode.Z, cooldown);
	}

	/**
	 * after this skill is done add script that set invicible back and remove this script 
	 * after 200
	 */
	@Override
	public void skillUpdate() {
		long now = System.currentTimeMillis();
		timeDuration = duration - (now - start);
		playerController.setSpeed(normalSpeed * 8.0);
		if (now - start > duration) {
			playerController.setSpeed(normalSpeed);
			parent.addScript(new BasicScript<Player>() {
				long start = System.currentTimeMillis();

				@Override
				public void update() {
					long now = System.currentTimeMillis();
					if (now - start > 200) {
						parent.getStatus().setInvincible(false);
						parent.removeScript(this);
					}
				}
			});
			skillDone();
		}
	}

	/**
	 * update the skill
	 */
	@Override
	protected void startSkill() {
		try {
			playerController = parent.getScript(PlayerController.class);
		} catch (ScriptNotFoundException e) {
			e.printStackTrace();
		}
		status = parent.getStatus();
		status.setInvincible(true);
		normalSpeed = playerController.getSpeed();
		mediaPlayer = new MediaPlayer(ResourceManager.getSound("sound/Suck 1.wav"));
		mediaPlayer.setStopTime(Duration.millis(400));
		mediaPlayer.play();
		start = System.currentTimeMillis();
	}

}
