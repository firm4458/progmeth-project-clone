package logic.player;

import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import logic.base.EntityStatus;
import logic.base.ScriptNotFoundException;
import logic.util.ResourceManager;

public class Dash extends PlayerSkill {
	
	private static long cooldown=500;
	private static long duration=50; 
	private long start;
	private double normalSpeed;
	private PlayerController playerController;
	private EntityStatus status;
	private MediaPlayer mediaPlayer;

	public Dash(Player parent) {
		super(KeyCode.Z, cooldown);
	}

	@Override
	public void skillUpdate() {
		long now = System.currentTimeMillis();
		playerController.setSpeed(normalSpeed*8.0);
		if(now-start > duration) {
			playerController.setSpeed(normalSpeed);
			System.out.println("GG");
			status.setInvincible(false);
			skillDone();
		}
	}

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
