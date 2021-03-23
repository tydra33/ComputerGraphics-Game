package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {
	private static long lastFrameTime;
	private static float delta;

	private static final int WIDTH = 1900;
	private static final int HEIGHT = 900;
	private static final int FPS = 120;

	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);

			Display.setTitle("Wardens of Steel");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		lastFrameTime = getCurrTime();
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void updateDisplay() {
		Display.sync(FPS);
		Display.update();
		long currFrameTime = getCurrTime();
		delta = (currFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currFrameTime;
	}

	public static void discardDisplay() {
		Display.destroy();
	}

	private static long getCurrTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSecs() {
		return delta;
	}
}
