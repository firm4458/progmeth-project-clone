import java.util.ArrayList;
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
			root.getChildren().add(temp);
		}
	}
	
	public static ArrayList<Meteor> getMeteors(){
		return GroupOfMeteors.meteors;
	}
	
	public static void update(Pane root, Player player) {
		for(int j = 0; j<meteors.size(); j++) {
			meteors.get(j).move();
			for(int i = 0; i<player.getBullets().size(); i++) {
				if(player.getBullets().get(i).getBoundsInParent().intersects(meteors.get(j).getBoundsInParent())) {
					player.getBullets().get(i).setDead(true);
					meteors.get(j).setDead(true);
					meteors.remove(j);
					player.getBullets().remove(i);
				}
			}
			
			if(meteors.get(j).checkOutOfPane()) {
				meteors.get(j).setDead(true);
				meteors.remove(j);
			}
		}
		
	}
}
