package gui;


import java.io.InputStream;

import application.GameManager;
import drawing.ImageSprite;
import drawing.TextSprite;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.IncompatibleScriptException;
import logic.base.Script;
import logic.base.Updatable;
import logic.util.ResourceManager;

public class ImageButton extends ToggleButton {
	private Image buttonImage;
	private Image mouseOverImage;
	private Image pressedImage;
	private GameObject gameObject;
	private ImageSprite imgSprite;
	private Text text; 
	private MediaPlayer mediaPlayer;
	
	public ImageButton(double width, double height, Image buttonImage, Image mouseOverImage, Image pressedImage) {
		super();
		setPrefSize(width, height);
		setBackground(null);
		setFocusTraversable(false);
		this.buttonImage = buttonImage;
		this.mouseOverImage = ((mouseOverImage==null)?  buttonImage : mouseOverImage);
		this.pressedImage = ((pressedImage==null) ? buttonImage : pressedImage) ;
		gameObject = new GameObject(0, 0);
		imgSprite = new ImageSprite(gameObject,this.buttonImage);
		gameObject.setSprite(imgSprite);
		
		ImageButton imgButton = this;
		
		gameObject.addScript(new Script() {
			GameObject parent;
			
			@Override
			public void update() {
				imgButton.setTranslateX(parent.getX()-parent.getScene().getSimpleCamera().getX());
				imgButton.setTranslateY(parent.getY()-parent.getScene().getSimpleCamera().getY());
				imgButton.setPrefSize(imgSprite.getWidth()*imgSprite.getScale(), imgSprite.getHeight()*imgSprite.getScale());
			}
			
			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}
			
			@Override
			public GameObject getParent() {
				return parent;
			}
		});
		
		imgSprite.setZ(99);
		imgSprite.setWidth(width);
		imgSprite.setHeight(height);
		setPickOnBounds(true);
		
		setOnMousePressed((evt)->{
			imgSprite.setImage(getPressedImage());
		});
		setOnMouseExited((evt)->{
			imgSprite.setImage(getButtonImage());
		});
		setOnMouseEntered((evt)->{
			imgSprite.setImage(getMouseOverImage());
		});
		setOnMouseReleased((evt)->{
			mediaPlayer = new MediaPlayer(ResourceManager.getSound("sound/Select 1.wav"));
			mediaPlayer.play();
			imgSprite.setImage(getButtonImage());
		});
		setPickOnBounds(true);
		GameManager.getInstance().getCurrentScene().addGameObject(gameObject);
	}
	
	public void disable() {
		setDisabled(true);
		setDisable(true);
		setVisible(false);
		imgSprite.setVisible(false);
	}
	
	public void enable() {
		setDisabled(false);
		setDisable(false);
		setVisible(true);
		imgSprite.setVisible(true);
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}



	public void createFollowText(String str,double relativeX, double relativeY) {
		GameObject gameObj = new GameObject(0,0);
		TextSprite textSprite = new TextSprite(gameObj, str, new Font("ARCADECLASSIC",30), getPrefWidth());
		gameObj.setSprite(textSprite);
		gameObj.addScript(new Script() {
			
			GameObject parent;
			
			@Override
			public void update() {
				parent.setX(relativeX+gameObject.getX());
				parent.setY(relativeY+gameObject.getY());
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}
			
		});
		textSprite.setZ(99);
		GameManager.getInstance().getCurrentScene().addGameObject(gameObj);
	}

	private Image getButtonImage() {
		return buttonImage;
	}

	private Image getMouseOverImage() {
		return mouseOverImage;
	}

	private Image getPressedImage() {
		return pressedImage;
	}
	
}
