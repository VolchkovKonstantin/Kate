package main.java.by.bsu.mag;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/init.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("QmCmax/k");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 361, 336));
        primaryStage.show();
    }


    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
}
