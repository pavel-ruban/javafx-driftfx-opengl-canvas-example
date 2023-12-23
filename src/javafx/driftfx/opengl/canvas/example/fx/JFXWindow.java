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
	private Scene display;
	private Parent root;
	private Controller controller;

	private final HashSet<KeyCode> activeKeys = new HashSet();

//	private static final GuiStateCache<GuiController> stateCache = new GuiStateCache("GuiState/Base");

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

//		window.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
//			@Override
//			public void handle(WindowEvent window) {
//				Platform.runLater(new Runnable() {
//					@Override
//					public void run() {
//						renderLoop.hook  = GLRenderer.getRenderer(getRenderPane());
//					}
//				});
//			}
//		});

//		renderLoop.contextID = org.eclipse.fx.drift.internal.GL.createSharedCompatContext(0);
//		org.eclipse.fx.drift.internal.GL.makeContextCurrent(renderLoop.contextID);
//		renderLoop.glCaps = GL.createCapabilities();
//		if (renderLoop.chain != null) {
//			renderLoop.chain.dispose();
//		}
//
//		Vec2i size = hook.getSize();
//		size = new Vec2i(500, 500);
//		renderLoop.width = size.x;
//		renderLoop.height = size.y;
//		renderLoop.chain = hook.createSwapchain(new SwapchainConfig(size, 2, PresentationMode.MAILBOX, renderLoop.transfer));
//
//		try {
//			renderLoop.renderTarget = renderLoop.chain.acquire();
//		} catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
////			}
////			if (tex <= 0)
//
//		int	tex = GLRenderer.getGLTextureId(renderLoop.renderTarget);
//		if (renderLoop.depthTex <= 0) {
//			renderLoop.depthTex = GL11.glGenTextures();
//			GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderLoop.depthTex);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
//			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL32.GL_DEPTH_COMPONENT32F, renderLoop.width, renderLoop.height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
//			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//		}
//
//		if (renderLoop.fb <= 0) {
//			renderLoop.fb = GL32.glGenFramebuffers();
//
//			GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, renderLoop.fb);
//			GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, tex, 0);
//			GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_DEPTH_ATTACHMENT, renderLoop.depthTex, 0);
//		}
//		else {
//			GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, renderLoop.fb);
//			GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, tex, 0);
////				GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_DEPTH_ATTACHMENT, depthTex, 0);
//		}
//
//		int status = GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER);
//		switch (status) {
//			case GL32.GL_FRAMEBUFFER_COMPLETE:
//				break;
//			case GL32.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
//				System.err.println("INCOMPLETE_ATTACHMENT!");
//				break;
//		}
//
//		GLFunctions.printGLErrors("DFX Framebuffer bind");
//		GL11.glClearColor(1, 0 , 0, 1);
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
////			intermediate.sendTo(fb);
////			intermediate.draw();
//		GLFunctions.printGLErrors("Framebuffer render to DFX");
//		//this.render(width, height);
//
//		GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, 0);
////			GL32.glDeleteFramebuffers(fb);
////			GL11.glDeleteTextures(depthTex);
//		GLFunctions.printGLErrors("DFX Framebuffer unbind");
//
//		renderLoop.chain.present(renderLoop.renderTarget);


	/*	window.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
		window.setResizable(false);

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML/GUI.fxml"));
		root = loader.load();
		controller = loader.getController();

		display = new Scene(root);

		display.setFill(Color.rgb(0x22, 0xaa, 0xff));

		display.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
		display.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

		Rectangle2D resolution = Screen.getPrimary().getVisualBounds();

		window.setTitle("Game Calendar - ReikaKalseki, 2020");
		window.setScene(display);
		double h = resolution.getHeight();
		//window.setWidth(resolution.getWidth()/2);
		//window.setHeight(resolution.getHeight()/2);
		//window.setX(resolution.getWidth()/4);
		//window.setY(resolution.getHeight()/4);
		//window.setHeight(Math.min(window.getHeight(), 1000));
		window.setMaxHeight(h-8);
		double y = h <= 1080 ? 4 : h-1080/2D+4;
		window.show();
		window.setY(y);
		window.setX((resolution.getWidth()-window.getWidth())/2);

		primary.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				try {
					stateCache.save(controller);
				}
				catch (IOException e) {
					System.err.println("Could not save GUI state:");
					e.printStackTrace();
				}
				Platform.exit();
			}
		});

		this.setStatus("Program initialized.");*/
//		controller.postInit(this.getHostServices());
//
//		double midX = window.getWidth()/2;
//		double midY = window.getHeight()/2;
//
//		for (TitledPane p : controller.getCollapsibleSections()) {
//			p.setExpanded(false);
//		}
//
//		stateCache.load(controller);

//		for (int i = 1; i < 100; ++i) {
//			int finalI = i;
//			delay(100 * i, () -> {
//				GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, renderLoop.fb);
//				GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, tex, 0);
//				GL11.glClearColor(finalI % 2 == 0 ? 1 : 0, 1, 0, 1);
//				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//				renderLoop.chain.present(renderLoop.renderTarget);
//			});
//		}
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
//
//	void setStatus(String value) {
//		controller.status.setText(value);
//	}
//
//	public void setScreenshots(List<? extends CalendarEvent> images) {
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				controller.setImages(images);
//			}
//		});
//	}

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

//	public static TextArea getDescriptionPane() {
//		return gui != null && gui.controller != null ? gui.controller.descriptionPane : null;
//	}
//
//	public static DFXInputHandler getMouseHandler() {
//		return gui != null && gui.controller != null ? gui.controller.mouseHandler : null;
//	}
//
//	public static SortingMode getSortingMode() {
//		return gui != null && gui.controller != null ? SortingMode.values()[gui.controller.sortList.getSelectionModel().getSelectedIndex()] : SortingMode.ALPHA;
//	}
//
//	boolean getCheckbox(GuiElement e) {
//		Node n = this.getOption(e);
//		return n != null && ((CheckBox)n).selectedProperty().get();
//	}
//
//	boolean isListEntrySelected(GuiElement e, String s) {
//		ListView n = this.getListView(e);
//		return n != null && n.getSelectionModel().isSelected(n.getItems().indexOf(s));
//	}
//
//	double getSliderValue(GuiElement e) {
//		Node n = this.getOption(e);
//		return n != null ? ((Slider)n).getValue() : 0;
//	}
//
//	private Node getOption(GuiElement e) {
//		return gui != null && gui.controller != null ? gui.controller.getOption(e) : null;
//	}
//
//	private ListView getListView(GuiElement e) {
//		return gui != null && gui.controller != null ? gui.controller.getListView(e) : null;
//	}
//
//	public boolean isKeyPressed(KeyCode key) {
//		return activeKeys.contains(key);
//	}
//
//	@Override
//	public void handle(javafx.event.Event event) {
//		controller.update(null);
//	}

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

//	public static Node replaceNode(ControllerBase con, Node rem, Node repl) {
//		Parent p = rem.getParent();
//		if (p instanceof Pane) {
//			ObservableList<Node> li = ((Pane)p).getChildren();
//			int idx = li.indexOf(rem);
//			li.add(idx, repl);
//			li.remove(rem);
//			con.replaceNode(rem, repl);
//			return repl;
//		}
//		else {
//			throw new IllegalArgumentException("You cannot replace nodes within non-pane nodes!");
//		}
//	}

}
