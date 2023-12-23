package javafx.driftfx.opengl.canvas.example.fx;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.eclipse.fx.drift.*;
import org.lwjgl.opengl.*;
import java.net.URL;
import java.util.ResourceBundle;
import org.eclipse.fx.drift.internal.GL;

public class Controller implements Initializable {

  public DriftFXSurface surface;
  public Button button;
  public Button button2;
  public BorderPane renderPane;
  private static long window = 0;
  protected Swapchain swapchain;

  private int curWidth = 0;
  private int curHeight = 0;
  private int fbo = 0;
  private int depthTex = 0;
  private final Vec2i size = new Vec2i(1000, 500);
  private final long context = GL.createContext(0, 3, 2);
  long ByteBuffer = 0;
  public DriftFXSurface renderingSurface;
//  DFXInputHandler mouseHandler;

  public Controller() {}

  public void onRenderClick() {
    System.out.println("clicked.");
  }

  @Override
  public void initialize(URL url, ResourceBundle resources) {
//    int x = 1;
//
//    surface.resize(1000, 500);
//    // you acquire the Renderer api by calling getRenderer on your surface
//    Renderer renderer = GLRenderer.getRenderer(surface);
//
//    // make context current
//    GL.makeContextCurrent(context);
//    GLCapabilities capabilities = org.lwjgl.opengl.GL.createCapabilities();
//
//    // threekt defaults
//    GL11.glEnable(GL32.GL_PROGRAM_POINT_SIZE);
////    GL11.glEnable(GL20.GL_POINT_SPRITE);
//
//    fbo = GL30.glGenFramebuffers();
//    depthTex = GL11.glGenTextures();
////    size. = surface.getMaxWidth();
////    size.y = surface.getMaxHeight();
//    // on your render thread you do the following:
//
//    // first you create your own opengl context & make it current
//
//
//
////    GLFW.glfwInit
////    GLData glData = new GLData();
////    glData.doubleBuffer = true;
////    glData.majorVersion = 3;
////    glData.minorVersion = 2;
////    glData.samples = 0;
////    glData.swapInterval = 1;
//
//    // in your render loop you manage your swapchain instance
//
//    // you can fetch the current size of the surface by asking the renderer
//    Vec2i size = renderer.getSize();
//
//    if (swapchain == null || size.x != swapchain.getConfig().size.x || size.y != swapchain.getConfig().size.y) {
//      // re-create the swapchain
//      if (swapchain != null) {
//        swapchain.dispose();
//      }
//      try {
//        swapchain = renderer.createSwapchain(new SwapchainConfig(size, 2, PresentationMode.MAILBOX, StandardTransferTypes.MainMemory));
//      } catch (RuntimeException e) {
//        throw new RuntimeException(e);
//      }
//    }
//
//    // to draw you acquire a RenderTarget from the swapchain
//    RenderTarget target = null; // this call is blocking, if there is no RenderTarget available it will wait until one gets available
//    try {
//      target = swapchain.acquire();
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
//
//    // now you setup a framebuffer with this texture and draw onto it
//    updateFBO(swapchain.getConfig().size, target);
//
//
//    bind();
////    GL.makeContextCurrent(context);
////    org.lwjgl.opengl.GL.createCapabilities();
//    //    GL.makeContextCurrent(context);
//
////    glClearColor(0f, 1f, 0f, 1f);
////    glDisable(GL_TEXTURE_2D);
////    glColor3b((byte) 122, (byte)0, (byte)22);
////    glBegin(GL_QUADS);
////    glVertex2i(0, 0);
////    glVertex2i(300, 0);
////    glVertex2i(300, 300);
////    glVertex2i(x,300);
////    glEnd();
////    glEnable(GL_TEXTURE_2D);
////    unit();
//
//    unbind();
//
//    // once you are finished with the frame you call present on the swapchain
//    swapchain.present(target);
  }

  protected void postInit() {
    System.out.println("Post-initializing GUI.");
    renderingSurface = new DriftFXSurface();
    button2 = new Button("testing...");
    renderingSurface.setMinWidth(500);
    renderingSurface.setMinHeight(500);
    button2.setMaxWidth(200);
    button2.setPrefWidth(200);
    renderingSurface.setPrefWidth(500);
    renderingSurface.setPrefHeight(500);
    VBox vbox = new VBox(10, renderingSurface, button2);
//    renderPane.getChildren().setAll(button2, renderer);
    renderPane.getChildren().add(vbox);
    AnchorPane ap = (AnchorPane) renderPane.getCenter();
//    ap.getChildren().setAll(renderer);

//    mouseHandler = new DFXInputHandler(renderer);
//    calendarOverlay.setOnMouseClicked(mouseHandler);
//    calendarOverlay.setOnMouseMoved(mouseHandler);
//    AnchorPane ap = (AnchorPane)renderField.getCenter();
//    ap.getChildren().setAll(renderer);
//    renderer.setPrefSize(800, 800);
//    renderField.setPadding(new Insets(0));
//
//    privacy.setMax(Main.getTimeline().getMaxPrivacyLevel());
//    privacy.setValue(privacy.getMax());
//    privacy.valueProperty().addListener(new ChangeListener<Number>() {
//      @Override
//      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//        privacy.setValue((int)Math.round(newValue.doubleValue()));
//        GuiController.this.update(GuiElement.PRIVACY.id);
//      }
//    });
//
//    this.turnOffPickOnBoundsFor(calendarButtonHolder);
//    advancedSelection.setLayoutX(800-advancedSelection.getWidth());
//    advancedSelection.setLayoutY(800-advancedSelection.getHeight()-3);
//
//    Labelling.instance.init(calendarOverlay);
//
//    //this.dynamicizeTextBoxes(root);
//    sortList.setItems(FXCollections.observableList(SortingMode.list()));
//    sortList.getSelectionModel().clearAndSelect(SortingMode.INDIVIDUAL.ordinal());
//    catList.setItems(FXCollections.observableList(ActivityCategory.getSortedNameList(JFXWindow.getSortingMode())));
//    catList.getSelectionModel().selectAll();
//    catList.setCellFactory(lv -> {
//      ListCell<String> cell = new CategoryListCell();
//      cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
//        catList.requestFocus();
//        if (!cell.isEmpty()) {
//          int index = cell.getIndex();
//          if (catList.getSelectionModel().getSelectedIndices().contains(index))
//            catList.getSelectionModel().clearSelection(index);
//          else
//            catList.getSelectionModel().select(index);
//          event.consume();
//        }
//        this.update(GuiElement.CATEGORIES.id);
//      });
//      return cell ;
//    });
//
//    for (NodeWrapper n : this.getOptionNodes()) {
//      if (n.object instanceof CheckBox)
//        ((CheckBox)n.object).selectedProperty().set(GuiElement.getByID(n.fxID).isDefaultChecked());
//    }
//
//    descriptionPane.setEditable(false);
//    descriptionPane.wrapTextProperty().set(true);
//
//    imageScroller.setFitToWidth(true);
//    ScrollBar descScroll = (ScrollBar)descriptionPane.lookup(".scroll-bar:vertical");
//    descScroll.setMinWidth(12);
//
//    this.setImages(null);
//
//    rightmostColumn.setFillWidth(true);
//
//    imageContainer.setPadding(Insets.EMPTY);
//    screenshotsTitled.setPadding(Insets.EMPTY);
//
//    for (Node n : optionsContainer.getChildrenUnmodifiable()) {
//      if (n instanceof Separator) {
//				/*
//				Region line = (Region)n.lookup(".line");
//				line.setPadding(Insets.EMPTY);
//				((Separator)n).setPadding(Insets.EMPTY);
//				 */
//        ((Separator)n).setValignment(VPos.CENTER);
//      }
//    }
//    optionsContainer.setSpacing(8);

    //this.update();
  }

  private void bind() {
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
  }

  private void unbind() {
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
  }

  private void dispose() {
    GL30.glDeleteFramebuffers(fbo);
    GL11.glDeleteTextures(depthTex);
    org.lwjgl.opengl.GL.setCapabilities(null);
    GL.destroyContext(context);
  }

  private void updateDepthTexSize(Vec2i size) {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTex);
    GL11.glTexImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      GL30.GL_DEPTH_COMPONENT32F,
      size.x,
      size.y,
      0,
      GL11.GL_DEPTH_COMPONENT,
      GL11.GL_FLOAT,
      ByteBuffer
    );

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  private void updateFBO(Vec2i size, RenderTarget target) {
    int targetTex = GLRenderer.getGLTextureId(target);
    if (size.x != curWidth || size.y != curHeight) {
      updateDepthTexSize(size);
    }
    GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
    GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, targetTex, 0);
    GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depthTex, 0);
  }

}
