package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Controller {

    @FXML
    private TextArea inputDataArea;

    @FXML
    private Button startBtn;

    @FXML
    private Button inputDataBtn;

    private void readInput() {
        String text = inputDataArea.getText();
        Algo algo = new Algo(new Scanner(text));
        algo.start();
    }

    private void openNewWindow() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("steps.fxml"));
            Stage stage = new Stage();
            stage.setTitle("QmCmax/k");
            stage.setScene(new Scene(root, 850, 550));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile(File file) {
        try {
            inputDataArea.setText("");
            Scanner s = new Scanner(file).useDelimiter("\\s+");
            while (s.hasNext()) {
                if (s.hasNextInt()) {
                    inputDataArea.appendText(s.nextInt() + " ");
                } else {
                    inputDataArea.appendText(s.next() + " ");
                }
            }
            startBtn.setDisable(false);
        } catch (FileNotFoundException ex) {
            inputDataArea.appendText("Sorry file not found");
            startBtn.setDisable(true);
        }
    }

    @FXML protected void startAction(ActionEvent event) {
        readInput();
        openNewWindow();
        Stage stage = (Stage) startBtn.getScene().getWindow();
        stage.close();
    }

    @FXML protected void fileChoose(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) inputDataBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        readFile(file);
    }



}
