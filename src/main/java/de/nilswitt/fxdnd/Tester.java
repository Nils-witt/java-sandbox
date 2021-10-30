package de.nilswitt.fxdnd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tester extends Application {


    public Tester() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();

        l.setOnMousePressed((event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                System.out.println("Pressed");
            }
        });
        l.setOnMouseReleased((event) -> {
            System.out.println("Released");
        });


    }
}
