package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("init.fxml"));
        primaryStage.setTitle("QmCmax/k");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 361, 336));
        primaryStage.show();
    }


    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
}
