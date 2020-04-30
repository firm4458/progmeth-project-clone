import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BackGround extends Canvas{
	GraphicsContext gc;
	DoubleProperty opacity = new SimpleDoubleProperty();
	DoubleProperty y = new SimpleDoubleProperty();
	double randomX[] = {Math.random(), Math.random(), Math.random(), Math.random(), Math.random()};
	double randomY[] = {Math.random(), Math.random(), Math.random(), Math.random(), Math.random()};
	double tempX = Math.random();
	double tempY = Math.random();
	
	//Opacity value
	Timeline timelineOpacity = new Timeline(
		new KeyFrame(Duration.seconds(0), 
				new KeyValue(opacity, 1)
		),
		new KeyFrame(Duration.seconds(3),
				new KeyValue(opacity, 0.2)
		)
	);

	//
	
	//Y value
	Timeline timelineMove = new Timeline(
		new KeyFrame(Duration.seconds(0), 
					new KeyValue(y, 0)
		),
		new KeyFrame(Duration.seconds(10),
					new KeyValue(y, getWidth()+5)
		)				
	);
 
	//
	
	public BackGround(int width, int height) {
		super(width, height);
		this.gc = this.getGraphicsContext2D();
	}
	
	public void update() {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());
		gc.setFill(Color.GHOSTWHITE.deriveColor(0, 1, 1, opacity.get()));
		
		for(int i = 1; i<6; i++) {
			gc.fillOval((getWidth()*randomX[i-1])%getWidth(), (y.get()+getHeight()*randomY[i-1])%(getHeight()+5), 3, 3);
			gc.fillOval((getWidth()*randomX[i-1]+600*tempX)%getWidth(), (y.get()+getHeight()*randomY[i-1]+600*tempY)%(getHeight()+5), 2, 2);
			gc.fillOval((getWidth()*randomX[i-1]+300*tempX)%getWidth(), (y.get()+getHeight()*randomY[i-1]+300*tempY)%(getHeight()+5), 1, 1);
		}
	}
	
	public void init() {
		timelineMove.setAutoReverse(false);
		timelineMove.setCycleCount(timelineMove.INDEFINITE);
		timelineOpacity.setAutoReverse(true);
		timelineOpacity.setCycleCount(timelineOpacity.INDEFINITE);
		timelineMove.play();
		timelineOpacity.play();
	}
}