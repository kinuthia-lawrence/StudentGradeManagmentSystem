package com.larrykin343.studentgrademanagmentsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class MainApp extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("Student Grade Management System");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/digitalLibrary.png")));
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
