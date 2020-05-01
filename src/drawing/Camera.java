package drawing;

import application.GameManager;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import logic.base.GameInterruptException;
import logic.base.GameObject;
import logic.base.Script;
import logic.base.ScriptNotFoundException;
import logic.util.ColliderBox;
import logic.util.IncompatibleScriptException;

public class Camera extends GameObject {
	private Canvas canvas;
	private boolean shake;
	public boolean isShake() {
		return shake;
	}
	public void setShake(boolean shake) {
		this.shake = shake;
	}
	private static final Script shaker = new Script() {
		
		Camera parent;
		private boolean shaking = false;
		private double offset;
		private double originalX;
		private double originalY;
		
		@Override
		public void update() throws GameInterruptException {
			if(shaking && offset<=3) {
				parent.translate(1.2, 1.2);
				offset+=1;
			}else if(shaking) {
				offset = 0;
				parent.setX(originalX);
				parent.setY(originalY);
				shaking = false;
			}else if(parent.isShake()) {
				shaking = true;
				parent.setShake(false);
				offset=0;
				originalX = parent.getX();
				originalY = parent.getY();
			}
			
		}

		@Override
		public GameObject getParent() {
			return parent;
		}

		@Override
		public void setParent(GameObject parent) throws IncompatibleScriptException {
			try {
				this.parent = (Camera)parent;
			}catch(ClassCastException e) {
				throw new IncompatibleScriptException("shaker", "must be attached to camera");
			}
		}

		@Override
		public void onDestroy() {
			
		}
		
	};
	public Camera(Canvas canvas) {
		super(0,0);
		this.canvas = canvas;
		Renderer.getInstance().setCamera(this);
		addScript(shaker);
	}
	public boolean isInCamera(GameObject gameObject) {
		BoundingBox bound = new BoundingBox(X,Y,GameManager.NATIVE_WIDTH,GameManager.NATIVE_HEIGHT);
		try {
			BoundingBox targetBound = gameObject.getScript(ColliderBox.class).getBound();
			return bound.intersects(targetBound);
		}catch(ScriptNotFoundException e) {
			return bound.contains(gameObject.getX(),gameObject.getY());
		}
	}
	public Canvas getCanvas() {
		return canvas;
	}
}
