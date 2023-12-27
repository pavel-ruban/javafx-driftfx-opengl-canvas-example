package javafx.driftfx.opengl.canvas.example.fx;

import com.sun.javafx.scene.control.LabeledText;
import javafx.collections.ObservableList;
import javafx.driftfx.opengl.canvas.example.rendering.RenderLoop;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.event.EventHandler;
import javafx.util.StringConverter;
import javafx.scene.input.ScrollEvent;
import org.eclipse.fx.drift.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

  public DriftFXSurface surface;
  public Button openButton;
  public Button openProjectButton ;
  public Button deleteButton;
  public Button saveButton;
  public Button saveAsButton;
  public Button closeButton;
  public Button exportButton;
  public MenuBar menu;
  public TreeView tree;
  public Button button2;
  public Pane renderPane;
  public VBox surfaceWrapper;

  public DriftFXSurface renderingSurface;
  public RenderLoop renderLoop;

  public Controller() {}

  public void onRenderClick() {
    System.out.println("clicked.");
  }
  private final Node rootIcon = new ImageView(
          new Image(getClass().getResourceAsStream("icons/folder-16.png"))
  );

  @Override
  public void initialize(URL url, ResourceBundle resources) {
    if (renderingSurface == null) {
      renderingSurface = new DriftFXSurface();
      renderingSurface.resize(renderPane.getWidth(), renderPane.getHeight());
      renderPane.widthProperty().addListener((obs, oldVal, newVal) -> {
        renderingSurface.resize((double) newVal, renderPane.getHeight());
        System.out.println("width size: " + newVal);
      });

      renderPane.heightProperty().addListener((obs, oldVal, newVal) -> {
        renderingSurface.resize(renderPane.getWidth(), (double) newVal);
        System.out.println("height size: " + newVal);
      });

      renderingSurface.setManaged(true);
      // Use default black pane to avoid flickering on gl canvas resize.
      surfaceWrapper = new VBox();
      surfaceWrapper.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

//          cacheSurface = new DriftFXSurface();
//          cacheSurface.resize(renderPane.getWidth(), renderPane.getHeight());
////          cacheSurface.setBlendMode(BlendMode.ADD);
//          renderingSurface.setBlendMode(BlendMode.SRC_OVER);
////          renderingSurface.toFront();
//          Group g = new Group();
//          g.getChildren().add(cacheSurface);
//          g.getChildren().add(renderingSurface);
      surfaceWrapper.getChildren().addAll(renderingSurface);

      renderPane.getChildren().add(surfaceWrapper);

    }

    Image img = new Image(getClass().getResourceAsStream("icons/add-document-24.png"));
    ImageView view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    openButton.setGraphic(view);
    openButton.setTooltip(new Tooltip("Open a file with data"));
    openButton.setOnAction(e -> {
      System.out.println("Open menu clicked.");
      FileChooser chooser = new FileChooser();
      chooser.setInitialDirectory(Paths.get(System.getProperty("user.dir")).toFile());
      var file = chooser.showOpenDialog(null);

      if (file == null) {
        return;
      }

      Path path = file.toPath();

      tree.setCellFactory(tv -> new TextFieldTreeCell<>(new StringConverter<TreeItemContainer>() {
        @Override
        public TreeItemContainer fromString(String text) {
          return new TreeItemContainer(text);
        }

        @Override
        public String toString(TreeItemContainer item) {
          return item.getName();
        }
      }));

      if (tree.getRoot() == null) {
        tree.setShowRoot(false);
        TreeItem<TreeItemContainer> root = new TreeItem<TreeItemContainer>(new TreeItemContainer("root"));
        tree.setRoot(root);
      }

      String[] testData = { "test data",  "test data",  "test data",  "test data",  "test data",  "test data",  "test data",  "test data"};
      TreeItem<TreeItemContainer> folder = new TreeItem<TreeItemContainer> (new TreeItemContainer(file.toString()), rootIcon);
      folder.setExpanded(true);
      for (String titem : testData) {
        TreeItem<TreeItemContainer> item = new TreeItem<TreeItemContainer> (new TreeItemContainer(titem));

        for (String stitem : testData) {
          TreeItem<TreeItemContainer> ch = new TreeItem<TreeItemContainer> (new TreeItemContainer(stitem));

          item.getChildren().add(ch);
        }

        folder.getChildren().add(item);
      }

      tree.getRoot().getChildren().add(folder);
      tree.setRoot(tree.getRoot());

      EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
//          handleMouseClicked(event);
      };

      tree.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

      System.out.println("Was opened " + file.toString());
    });

    img = new Image(getClass().getResourceAsStream("icons/project-management.png"));
    view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    openProjectButton.setGraphic(view);
    openProjectButton.setTooltip(new Tooltip("Open a project"));

    img = new Image(getClass().getResourceAsStream("icons/diskette-24.png"));
    view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    saveButton.setGraphic(view);
    saveButton.setTooltip(new Tooltip("Save"));

    img = new Image(getClass().getResourceAsStream("icons/diskette-24.png"));
    view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    saveAsButton.setGraphic(view);
    saveAsButton.setTooltip(new Tooltip("Save as"));

    img = new Image(getClass().getResourceAsStream("icons/document-delete-24.png"));
    view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    deleteButton.setGraphic(view);
    deleteButton.setTooltip(new Tooltip("Delete"));

    img = new Image(getClass().getResourceAsStream("icons/export-24.png"));
    view = new ImageView(img);
    view.setFitHeight(24);
    view.setPreserveRatio(true);
    exportButton.setGraphic(view);
    exportButton.setTooltip(new Tooltip("Export"));
  }

  protected void postInit() {
    System.out.println("Post-initializing GUI.");
  }

  private void mouseRelease(MouseEvent me) {
    if (me.getButton().name() == "PRIMARY") {
//      glCanvas.onMouseUp(me);
    }
  }

  private void mouseDrag(MouseEvent me) {
    if (me.getButton().name() == "PRIMARY") {
//      glCanvas.onMouseMove(me);
    }
  }

  private void mouseScroll(ScrollEvent se) {
//    glCanvas.onMouseWheel(se);
  }

  private void mousePress(MouseEvent me) {
    if (me.isPrimaryButtonDown()) {
//      glCanvas.onMouseDown(me);
    }
  }

}
