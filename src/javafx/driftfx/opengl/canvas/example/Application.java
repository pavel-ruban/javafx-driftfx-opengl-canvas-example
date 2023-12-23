package javafx.driftfx.opengl.canvas.example;

import javafx.driftfx.opengl.canvas.example.fx.JFXWindow;
import javafx.driftfx.opengl.canvas.example.rendering.RenderLoop;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * This class controls all aspects of the application's execution
 */
public class Application {

	/**
	 * Entry point.
	 *
	 * @param args
	 *   Arguments.
	 */
	public static void main(String[] args) {

		// Start opengl renderer thread.
		RenderLoop renderer = new RenderLoop();
		renderer.start();

		// Start ordinary javafx app thread.
		JFXWindow.create(renderer);

		renderer.close();
		System.out.print("Exiting program");
		System.exit(0);
	}

	private static final UncaughtExceptionHandler defaultErrorHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			System.out.println("Thread encountered an uncaught exception:");
			System.out.println(t.toString());
			e.printStackTrace();
			javafx.application.Platform.exit();
		}
	};
}
