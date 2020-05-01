package logic.player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import application.GUI;
import application.GameManager;
import application.GameScene;
import application.NormalLevelScene;
import drawing.ImageSprite;
import drawing.Sprite;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.SceneChangeInterruptException;
import logic.base.Script;
import logic.item.Item;
import logic.util.ColliderBox;
import logic.util.CollisionDetection;
import logic.util.IncompatibleScriptException;
import logic.util.animation.AnimationState;
import logic.util.animation.Animator;

public class Player extends GameObject {
	
	private static AnimationState idleState;
	private static AnimationState goLeftState;
	private static AnimationState goRightState;
	private static int HealthPoint;
	private static int MaxHealthPoint = 3;
	private static boolean upgradeAmmo = false;
	private static int upgradeTimeAmmo = 0;
	
	static {
		Image fullimg = new Image("img/ship.png",400,240,true,true);
		Image[] images = new Image[20];
		int index = 0;
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,0,80,120);
		}
		for(int i=0;i<5;++i) {
			images[index++] = new WritableImage(fullimg.getPixelReader(),i*80,120,80,120);
		}
		idleState = new AnimationState("idle",new Image[] {images[2],images[2],images[7],images[7]}, 
				new TreeMap<String,AnimationState>());
		goLeftState = new AnimationState("goLeft",new Image[] {images[0],images[0],images[5],images[5]}, 
				new TreeMap<String,AnimationState>());
		goRightState = new AnimationState("goRight",new Image[] {images[4],images[4],images[9],images[9]}, 
				new TreeMap<String,AnimationState>());
		idleState.putTrigger("goLeft", goLeftState);
		idleState.putTrigger("goRight", goRightState);
		goRightState.putTrigger("idle", idleState);
		goRightState.putTrigger("goLeft", goLeftState);
		goLeftState.putTrigger("idle", idleState);
		goLeftState.putTrigger("goRight", goRightState);
	}

	public Player(double X, double Y) {
		super(X, Y);
		HealthPoint = MaxHealthPoint;
		WritableImage img = new WritableImage(new Image("img/ship.png",400,240,true,true).getPixelReader(),0,0,80,120);
		sprite = new ImageSprite(this,img);
		animator = new Animator((ImageSprite)sprite,idleState);
		addScript(new PlayerController()).addScript(animator).addScript(new BulletShooter()).addScript(new ColliderBox(20,0,40,40));
		NormalLevelScene scene = (NormalLevelScene)GameManager.getInstance().getCurrentScene();
		
		//Collide with Meteors
		addScript(new CollisionDetection(scene.groupOfMeteors.getMeteors()) {
			
			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				for(GameObject meteor: targets) {
					meteor.destroy();
					HealthPoint--;
				}
				if(HealthPoint <= 0)
					throw new SceneChangeInterruptException(GUI.menuScene);
			}
		});
		////////
		
		//Collide with Items
		addScript(new CollisionDetection(scene.groupOfItems.getItems()) {

			@Override
			public void onCollision(ArrayList<GameObject> targets) throws GameInterruptException {
				for(GameObject item: targets) {
					if(!item.isDestroyed() && item.getClass() == Item.class) {
						String itemName = ((Item)item).getItemName();
						switch(itemName) {
							case "Powerup_Health":
								healing(1);
								break;
							case "Powerup_Ammo":
								upgradeAmmo = true;
								upgradeTimeAmmo = 1800;
								break;
						}
						item.destroy();
					}
				}
			}
			
		});
		///////
		
		//Count UpgradeTimeAmmo
		addScript(new Script() {
			GameObject parent;
			
			@Override
			public void update() throws GameInterruptException {
				if(upgradeAmmo) {
					upgradeTimeAmmo--;
					if(upgradeTimeAmmo==0)
						upgradeAmmo = false;
				}
			}

			@Override
			public GameObject getParent() {
				return parent;
			}

			@Override
			public void setParent(GameObject parent) throws IncompatibleScriptException {
				this.parent = parent;
			}

			@Override
			public void onDestroy() {
				
			}
			
		});
		//////
	}
	
	public void healing(int heal) {
		if(HealthPoint < MaxHealthPoint) {
			HealthPoint+=heal;
		}
	}
	
	public int getHealthPoint() {
		return HealthPoint;
	}
	
	public int getMaxHealthPoint() {
		return MaxHealthPoint;
	}
	
	public void setUpgradeAmmo(boolean upgrade) {
		upgradeAmmo = upgrade;
	}
	
	public boolean getUpgradeAmmo() {
		return upgradeAmmo;
	}
	
	public int getUpgradeTimeAmmo() {
		return upgradeTimeAmmo;
	}
	
	public void setUpgradeTimeAmmo(int time) {
		upgradeTimeAmmo = time;
	}
	
	public Animator animator;

}
