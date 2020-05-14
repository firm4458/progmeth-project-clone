package drawing;

import java.util.ArrayList;
import java.util.Comparator;

import application.GameScene;
import drawing.base.Renderable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Renderer {
	public static final ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	private static final Renderer instance = new Renderer();
	private static int renderCount;
	private static Image background;
	private SimpleCamera camera;
	private GraphicsContext gc;

	public BoundingBox bb;

	public GraphicsContext getGc() {
		return gc;
	}

	public void setGc(GraphicsContext gc) {
		this.gc = gc;
	}

	static {
		background = new Image("img/desert-backgorund.png", 1000, 1000, true, true);
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

	public void render() {
		render(gc);
	}

	public void render(GameScene gameScene) {
		Canvas canvas = gameScene.getCanvas();
		SimpleCamera camera = gameScene.getSimpleCamera();
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// fill entire screen with black (erasing previous frame's graphics)
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// render each Renderable
		ArrayList<Renderable> renderables = gameScene.getAllGameObject().getRenderables();
		renderables.sort(new Comparator<Renderable>() {

			@Override
			public int compare(Renderable o1, Renderable o2) {
				return o1.getZ() - o2.getZ();
			}

		});

		renderables.forEach((renderable) -> {
			if (renderable.isVisible()) {
				renderable.draw(gc, camera);
			}
		});
	}

	public void render(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, camera.getCanvas().getWidth(), camera.getCanvas().getHeight());
//		gc.drawImage(background, -10-camera.getX(), -10-camera.getY());
		for (Renderable renderable : renderables) {
			if (renderable.isVisible()) {
				renderable.draw(gc, camera);
			}
		}
		if (bb != null) {
			gc.fillRect(bb.getMinX(), bb.getMinY(), bb.getWidth(), bb.getHeight());
		}
		if (++renderCount >= 60) {
			clearDestroyedRenderable();
		}
	}

	public static void clearDestroyedRenderable() {
		renderables.removeIf(renderable -> renderable.isDestroyed());
	}

	public void setCamera(SimpleCamera camera) {
		this.camera = camera;
	}

	public SimpleCamera getCamera() {
		return camera;
	}

	/*
	 * public static void setBgOffset(double bgOffset) { Renderer.bgOffset =
	 * bgOffset; } public static double getBgOffset() { return bgOffset; }
	 */
	public void reset() {
		renderables.clear();
	}
}
