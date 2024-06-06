package com.general.equiplogs;

import com.general.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Leave like this for now. Might require changes in the future
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);

        stage.setMinWidth(1200);
        stage.setMinHeight(675);

        stage.setTitle("Loguri Echipamente");
        stage.setScene(scene);
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("images/database.png")));
        stage.show();

        new MainController().setStage(stage);
    }

    // Window launch
    public static void main(String[] args) {
        launch();
    }
}