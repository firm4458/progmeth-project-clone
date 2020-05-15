package drawing;

import java.util.ArrayList;
import java.util.Comparator;

import application.GameScene;
import drawing.base.Renderable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer {
	private static final Renderer instance = new Renderer();

	private Renderer() {
		super();
	}

	public static Renderer getInstance() {
		return instance;
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

}
