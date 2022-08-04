package com.reine.prohibitimage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class BootApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BootApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("禁止图片生成");
        stage.setResizable(false);
        stage.setScene(scene);
        Image image = new Image("images/prohibit.png");
        stage.getIcons().add(image);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}