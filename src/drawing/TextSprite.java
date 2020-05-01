package drawing;

import application.GameManager;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.base.GameObject;

public class TextSprite extends Sprite {
	String text;
	double size;
	private double maxWidth;
	public TextSprite(GameObject parent, String text,double size,double maxWidth) {
		super(parent);
		this.text = text;
		this.size = size;
		this.maxWidth = maxWidth;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	@Override
	public void draw(GraphicsContext gc, Camera camera) {
		double absoluteX = parent.getX()+relativeX-camera.getX();
		double absoluteY = parent.getY()+relativeY-camera.getY();
		double XScale = gc.getCanvas().getWidth()/GameManager.NATIVE_WIDTH;
		double YScale = gc.getCanvas().getHeight()/GameManager.NATIVE_HEIGHT;
		gc.setFont(new Font(size*Math.min(XScale, YScale)));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(javafx.scene.paint.Color.WHITE);
		gc.fillText(text, absoluteX*XScale, absoluteY*YScale, maxWidth/2*XScale);
	}
	public double getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}
}
