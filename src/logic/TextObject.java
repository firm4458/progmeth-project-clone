package logic;

import drawing.TextSprite;
import javafx.scene.text.Font;
import logic.base.GameObject;

public class TextObject extends GameObject {
	public TextObject(double X, double Y, String text, Font font,double maxWidth) {
		super(X,Y);
		TextSprite sprite = new TextSprite(this, text, font,maxWidth);
		this.sprite = sprite;
	}
}
