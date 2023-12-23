package javafx.driftfx.opengl.canvas.example;

import javafx.driftfx.opengl.canvas.example.fx.JFXWindow;
import javafx.driftfx.opengl.canvas.example.rendering.RenderLoop;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.eclipse.fx.drift.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GLCapabilities;

import java.lang.Thread.UncaughtExceptionHandler;

import static java.lang.Thread.sleep;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

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
