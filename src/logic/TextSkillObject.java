package logic;

import drawing.TextSprite;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.base.BasicScript;
import logic.base.Dio;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.ScriptNotFoundException;
import logic.player.Player;

/**
 * This class extends TextObject and implements Dio in order to update during use the world skill.
 * @author user
 *
 */

public class TextSkillObject extends TextObject implements Dio {

	/**
	 * It construct like TextObject and set z to 99 in order to draw it in the front
	 * If skill parameter is DashText add Scrpit that update text to cooldown dash skill.
	 * If skill is using, color will be green.
	 * If skill is cooldown, color will be red.
	 * If skill parameter is TheWorldText add Scrpit that update text to cooldown theWorld skill.
	 * If skill is using, color will be green.
	 * If skill is cooldown, color will be red.
	 * @param X This is a position in x-axis
	 * @param Y This is a position in y-axis
	 * @param skill This is a name of the skill 
	 * @param text This use for initial the text
	 * @param font This use to set font
	 * @param maxWidth This use to set the maxWidth
	 * @param player This is player from a Scene
	 */
	public TextSkillObject(double X, double Y, String skill, String text, Font font, double maxWidth, Player player) {
		super(X, Y, text, font, maxWidth);
		this.getSprite().setZ(99);
		if (skill.equals("DashText")) {
			addScript(new BasicScript<GameObject>() {

				@Override
				public void update() throws GameInterruptException {
					TextSprite ts = (TextSprite) (parent.getSprite());
					try {
						if (!player.getDashSkillIsActive()) {
							ts.setColor(Color.RED);
							ts.setText("X");
						} else if (player.getDashSkillIsUsing()) {
							ts.setColor(Color.GREEN);
							ts.setText("" + player.getDashTime());
						} else if (player.getDashSkillCooldown() == 0) {
							ts.setColor(Color.GREEN);
							ts.setText("READY");
						} else {
							ts.setColor(Color.RED);
							ts.setText("" + player.getDashSkillCooldown());
						}
					} catch (ScriptNotFoundException e) {
						e.printStackTrace();
					}
				}

			});
		} else if (skill.equals("TheWorldText")) {
			addScript(new BasicScript<GameObject>() {

				@Override
				public void update() throws GameInterruptException {
					TextSprite ts = (TextSprite) (parent.getSprite());
					try {
						if (!player.getTheWorldIsActive()) {
							ts.setColor(Color.RED);
							ts.setText("X");
						} else if (player.getTheWorldSkillIsUsing()) {
							ts.setColor(Color.GREEN);
							ts.setText("" + player.getTheWorldTime());
						} else if (player.getTheWorldSkillCooldown() == 0) {
							ts.setColor(Color.GREEN);
							ts.setText("READY");
						} else {
							ts.setColor(Color.RED);
							ts.setText("" + player.getTheWorldSkillCooldown());
						}
					} catch (ScriptNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
