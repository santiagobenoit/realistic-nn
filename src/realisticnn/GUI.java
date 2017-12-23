package realisticnn;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;

public class GUI extends Application {

    public static volatile ObservableMap<Neuron, Circle> circles;
    public static volatile ObservableMap<Pair<Neuron, Neuron>, Line> lines;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        circles = FXCollections.observableHashMap();
        lines = FXCollections.observableHashMap();
        StackPane stack = new StackPane();
        circles.addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Neuron, ? extends Circle> change) {
                stack.getChildren().clear();
                //int i = 8+9;
                stack.getChildren().addAll(circles.values());
                stack.getChildren().addAll(lines.values());
            }
        });
        lines.addListener(new MapChangeListener<>() {
            @Override
            public void onChanged(Change<? extends Pair<Neuron, Neuron>, ? extends Line> change) {
                //stack.getChildren().clear();
                //stack.getChildren().addAll(circles.values());
                //stack.getChildren().addAll(lines.values());
            }
        });
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(stack);
        Scene scene = new Scene(scroll, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(() -> {
            Main.main(null);
        });
    }
}
