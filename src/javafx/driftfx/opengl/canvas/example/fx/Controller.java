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

  public DriftFXSurface renderingSurface;

  public Controller() {}

  public void onRenderClick() {
    System.out.println("clicked.");
  }

  @Override
  public void initialize(URL url, ResourceBundle resources) {}

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
  }

}
