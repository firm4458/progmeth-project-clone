package drawing;
import java.util.ArrayList;
import java.util.Comparator;

import drawing.base.Renderable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Renderer {
	private static final ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	private static final Renderer instance = new Renderer();
	private static int renderCount;
	private static Image background;
	private Camera camera;
	
	static {
		background = new Image("img/desert-backgorund.png",1000,1000,true,true);
	}
	
	private Renderer() {
		super();
		renderCount = 0;
	}
	public static Renderer getInstance() {
		return instance;
	}
	public void add(Renderable renderable) {
		renderables.add(renderable);
		renderables.sort(new Comparator<Renderable>() {

			@Override
			public int compare(Renderable o1, Renderable o2) {
				return o1.getZ() - o2.getZ();
			}
			
		});
	}
	public void render(GraphicsContext gc) {
		
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//		gc.drawImage(background, -10-camera.getX(), -10-camera.getY());
		for(Renderable renderable : renderables) {
			if(renderable.isVisible()) {
				renderable.draw(gc,camera);
			}
		}
		if(++renderCount >= 60) {
			clearDestroyedRenderable();
		}
	}
	public void clearDestroyedRenderable() {
		renderables.removeIf(renderable -> renderable.isDestroyed());
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public Camera getCamera() {
		return camera;
	}
	/*public static void setBgOffset(double bgOffset) {
		Renderer.bgOffset = bgOffset;
	}
	public static double getBgOffset() {
		return bgOffset;
	}*/
}
