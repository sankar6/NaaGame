package com.naagame.editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Objects;

public class Main extends Application {
    public static Window window;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("main.fxml")));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("NaaGame - Create your own games!");
        stage.setMaximized(true);
        stage.show();
    }
}
