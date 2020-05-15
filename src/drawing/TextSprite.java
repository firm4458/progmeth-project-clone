package drawing;

import application.GameManager;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.base.GameObject;

public class TextSprite extends Sprite {
	private String text;
	private Font font;
	private double maxWidth;
	private Color color;

	public TextSprite(GameObject parent, String text, Font font, double maxWidth) {
		super(parent);
		this.text = text;
		this.font = font;
		this.maxWidth = maxWidth;
		this.color = Color.WHITE;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	private boolean isCenterAligned = true;

	public boolean isCenterAligned() {
		return isCenterAligned;
	}

	public void setCenterAligned(boolean isCenterAligned) {
		this.isCenterAligned = isCenterAligned;
	}

	@Override
	public void draw(GraphicsContext gc, SimpleCamera camera) {
		double absoluteX = parent.getX() + relativeX - camera.getX();
		double absoluteY = parent.getY() + relativeY - camera.getY();
		double XScale = gc.getCanvas().getWidth() / GameManager.NATIVE_WIDTH;
		double YScale = gc.getCanvas().getHeight() / GameManager.NATIVE_HEIGHT;
		gc.setFont(font);
		if (isCenterAligned) {
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
		} else {
			gc.setTextAlign(TextAlignment.LEFT);
			gc.setTextBaseline(VPos.BASELINE);
		}
		gc.setFill(color);
		gc.fillText(text, absoluteX * XScale, absoluteY * YScale, maxWidth * XScale);
	}

	public double getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
	}
}
