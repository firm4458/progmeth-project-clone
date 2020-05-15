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

public class TextSkillObject extends TextObject implements Dio {

	public TextSkillObject(double X, double Y, String skill, String text, Font font, double maxWidth, Player player) {
		super(X, Y, text, font, maxWidth);
		this.getSprite().setZ(99);
		if(skill.equals("DashText")) {
			addScript(new BasicScript<GameObject>() {
				
				@Override
				public void update() throws GameInterruptException {
					TextSprite ts = (TextSprite) (parent.getSprite());
					try {
						if(!player.getDashSkillIsActive()) {
							ts.setColor(Color.RED);
							ts.setText("X");
						}
						else if(player.getDashSkillIsUsing()) {
							ts.setColor(Color.GREEN);
							ts.setText(""+player.getDashTime());
						}
						else if(player.getDashSkillCooldown() == 0){
							ts.setColor(Color.GREEN);
							ts.setText("READY");
						}
						else {
							ts.setColor(Color.RED);
							ts.setText(""+player.getDashSkillCooldown());
						}
					} catch (ScriptNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
		else if(skill.equals("TheWorldText")){
			addScript(new BasicScript<GameObject>() {
				
				@Override
				public void update() throws GameInterruptException {
					TextSprite ts = (TextSprite) (parent.getSprite());
					try {
						if(!player.getTheWorldIsActive()) {
							ts.setColor(Color.RED);
							ts.setText("X");
						}
						else if(player.getTheWorldSkillIsUsing()) {
							ts.setColor(Color.GREEN);
							ts.setText(""+player.getTheWorldTime());
						}
						else if(player.getTheWorldSkillCooldown() == 0){
							ts.setColor(Color.GREEN);
							ts.setText("READY");
						}
						else {
							ts.setColor(Color.RED);
							ts.setText(""+player.getTheWorldSkillCooldown());
						}
					} catch (ScriptNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}


}
