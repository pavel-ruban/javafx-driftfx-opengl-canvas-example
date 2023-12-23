package javafx.driftfx.opengl.canvas.example.fx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.driftfx.opengl.canvas.example.rendering.RenderLoop;
import javafx.util.Duration;
import org.eclipse.fx.drift.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.concurrent.Task;

public class JFXWindow extends Application {

	private static JFXWindow gui;
	private static RenderLoop renderLoop;
	private static boolean isLoaded = false;
	private Stage window;
	private Controller controller;

	private final HashSet<KeyCode> activeKeys = new HashSet();

	public JFXWindow() {
		gui = this;
	}

	@Override
	public void start(Stage primary) throws Exception {
		this.init(primary);
	}

	private void init(Stage primary) throws IOException {
		System.out.println("Initializing GUI.");
		window = primary;
		window.setTitle("app javafx / driftfx opengl canvas example");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
		Parent root = loader.load();
		window.setScene(new Scene(root, 1920, 1080));
		controller = loader.getController();
		controller.postInit();
		isLoaded = true;

		// Make rendering thread acknowledge the fx app is rendered & ready for gl calls.
		window.setOnShown((event) -> {
//			new Timeline(new KeyFrame(
//				Duration.millis(5000),
//				actionEvent -> {
					// You should run this call in main javafx QuantumRenderer thread or the texture of
					// swapchain acquire later call will be corrupted, at least in linux with main memory target.
					renderLoop.hook = GLRenderer.getRenderer(getRenderPane());
					System.out.println("Hook init");
//				})
//			).play();

			System.out.println("Stage Shown");
		});

		window.show();
	}

	public static void delay(long millis, Runnable continuation) {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try { Thread.sleep(millis); }
				catch (InterruptedException e) { }
				return null;
			}
		};
		sleeper.setOnSucceeded(event -> continuation.run());
		new Thread(sleeper).start();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.out.println("Closing window");
	}

	public static void create(RenderLoop loop) {
		renderLoop = loop;
		launch();
	}

	public static JFXWindow getGUI() {
		return gui;
	}

	public static boolean isLoaded() {
		return isLoaded;
	}

	public static DriftFXSurface getRenderPane() {
		return gui != null && gui.controller != null ? gui.controller.renderingSurface : null;
	}

	public static ArrayList<Node> getRecursiveChildren(Parent root) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		addAllChildren(root, nodes);
		return nodes;
	}

	private static void addAllChildren(Parent p, ArrayList<Node> nodes) {
		for (Node n : getDirectChildren(p)) {
			nodes.add(n);
			if (n instanceof Parent)
				addAllChildren((Parent)n, nodes);
		}
	}

	private static Collection<Node> getDirectChildren(Parent p) {
		Collection<Node> ret = new ArrayList();
		ret.addAll(p.getChildrenUnmodifiable());
		if (p instanceof SplitPane) {
			ret.addAll(((SplitPane)p).getItems());
		}
		if (p instanceof Accordion) {
			ret.addAll(((Accordion)p).getPanes());
		}
		if (p instanceof TitledPane) {
			ret.add(((TitledPane)p).getContent());
		}
		return ret;
	}

}
