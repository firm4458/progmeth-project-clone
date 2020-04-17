import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GroupOfMeteors {
	private static ArrayList<Meteor> meteors = new ArrayList<>();
	private static int MAX_METEORS = 10;
	
	public static void generate(Pane root) {
		for(int i = 0; i<MAX_METEORS-meteors.size(); i++) {
			Meteor temp = new Meteor(20, 20, Color.DARKRED);
			temp.setTranslateX(600*Math.random() - 20);
			meteors.add(temp);
		}
	}
	
	public static ArrayList<Meteor> getMeteors(){
		return GroupOfMeteors.meteors;
	}
	
	public static void update(Pane root, Player player) {
		Iterator<Meteor> meteorIt = meteors.iterator();
		while (meteorIt.hasNext()) {
			Meteor meteor = meteorIt.next();
			meteor.move();
			
			Iterator<Bullet> bulletIt = player.getBullets().iterator();
			while (bulletIt.hasNext()) {
				Bullet bullet = bulletIt.next();
				if (bullet.getBoundsInParent().intersects(meteor.getBoundsInParent())) {
					GUI.addScore(1);
					bullet.setDead(true);
					meteor.setDead(true);
					bulletIt.remove();
					break;
				}
			}

			if (meteor.checkOutOfPane()) {
				meteor.setDead(true);
			}

			if (meteor.getDead()) {
				meteorIt.remove();
			}
		}
		
	}
}
