package logic;

import drawing.TextSprite;
import javafx.scene.text.Font;
import logic.base.GameObject;

public class TextObject extends GameObject {
	public TextObject(double X, double Y, String text, Font font, double maxWidth) {
		super(X, Y);
		TextSprite sprite = new TextSprite(this, text, font, maxWidth);
		this.sprite = sprite;
	}

	public TextObject(double X, double Y, String name, String text, Font font, double maxWidth) {
		super(X, Y, name);
		TextSprite sprite = new TextSprite(this, text, font, maxWidth);
		this.sprite = sprite;
	}

	public void setText(String text) {
		((TextSprite) sprite).setText(text);
	}

	public String getText() {
		return ((TextSprite) sprite).getText();
	}
}
