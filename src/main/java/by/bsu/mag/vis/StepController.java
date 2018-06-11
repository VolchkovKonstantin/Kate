package by.bsu.mag.vis;

import by.bsu.mag.algo.Algo;
import by.bsu.mag.algo.Log;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;

public class StepController {

    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button resultBtn;
    @FXML
    private AnchorPane anchor;

    private CustomStackedBarChart hist;
    private Line lbLine;
    @FXML
    private Label situation;
    @FXML
    private Label kLabel;
    @FXML
    private Button toMenuBtn;

    private NumberAxis numberAxise;

    private int stepNumber;

    private int MAX_STEP;

    @FXML
    public void initialize() {
        CategoryAxis  categoryAxis = new CategoryAxis();
        categoryAxis.setLabel("Machines");
        categoryAxis.setSide(Side.BOTTOM);
        numberAxise = new NumberAxis();
        numberAxise.setLabel("Time");
        numberAxise.setSide(Side.LEFT);
        hist = new CustomStackedBarChart(categoryAxis, numberAxise);
        hist.setLayoutX(14.0);
        hist.setLayoutY(14.0);
        hist.prefHeight(400.0);
        hist.prefWidth(743.0);
        anchor.getChildren().add(hist);
        lbLine = new Line();
        lbLine.setDisable(true);
        lbLine.setStroke(Color.RED);
        lbLine.setStartY(153.0);
        lbLine.setEndY(153.0);
        lbLine.setStartX(30.0);
        lbLine.setEndX(750.0);
        anchor.getChildren().add(lbLine);

        kLabel.setText("K = " + Algo.MAX_K);
        stepNumber = 0;
        render(stepNumber);
        MAX_STEP = Algo.history.size() - 1;
    }


    @FXML
    protected void prevAction(ActionEvent event) {
        if (stepNumber == 1) {
            prevBtn.setDisable(true);
        }
        if (stepNumber > 0) {
            stepNumber--;
        }
        if (stepNumber != MAX_STEP) {
            nextBtn.setDisable(false);
            resultBtn.setDisable(false);
        }
        render(stepNumber);
    }

    @FXML
    protected void nextAction(ActionEvent event) {
        if (prevBtn.isDisable()) {
            prevBtn.setDisable(false);
        }
        if (stepNumber == MAX_STEP - 1) {
            nextBtn.setDisable(true);
            resultBtn.setDisable(true);
        }
        stepNumber++;
        render(stepNumber);
    }

    @FXML
    protected void resultAction(ActionEvent event) {
            nextBtn.setDisable(true);
            resultBtn.setDisable(true);
            prevBtn.setDisable(false);
            stepNumber = MAX_STEP;
        render(MAX_STEP);
    }

    private void openMenuWindow() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/init.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("QmCmax/k");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 361, 336));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void menuAction(ActionEvent event) {
        Controller.backup = Algo.backup;
        openMenuWindow();
        Stage stage = (Stage) toMenuBtn.getScene().getWindow();
        stage.close();
    }

    private void render(int step) {
        hist.setAnimated(false);
        Log log = Algo.history.get(step);
        ObservableList li = hist.getData();

        List<XYChart.Series<String, Number>> xis = new ArrayList<>();
        for (int i = 0; i < Algo.MAX_K; i++) {
            xis.add(new XYChart.Series());
        }

        List<List<Integer>> nCup = log.getNCup();
        for (int j = 0; j < Algo.M; j++) {
            List<Integer> works = nCup.get(j);
            if (works.isEmpty()) {
                for (int i = 0; i < Algo.MAX_K; i++) {
                    xis.get(i).getData().add(new XYChart.Data<String, Number>("M" + (j + 1), 0));
                }
            }
            for (int i = 0; i < works.size(); i++) {
                Integer numberWork = works.get(i);
                ObservableList<XYChart.Data<String, Number>> item = xis.get(i).getData();
                item.add(new XYChart.Data<String, Number>("M" + (j + 1), Algo.T[numberWork][j]));
            }
        }
        li.setAll(xis);

        numberAxise.setAutoRanging(true);
        hist.applyCss();
        hist.getParent().layout();
        hist.layoutPlotChildren();
        double yLB = hist.getYAxis().getDisplayPosition(log.getLB());
        if (yLB <= 0) {
            numberAxise.setAutoRanging(false);
            numberAxise.setUpperBound(Algo.history.get(step).getLB().doubleValue());
            hist.applyCss();
            hist.getParent().layout();
            hist.layoutPlotChildren();

            yLB = 0;
        } else {
            numberAxise.setAutoRanging(true);
            hist.applyCss();
            hist.getParent().layout();
            hist.layoutPlotChildren();

            yLB = hist.getYAxis().getDisplayPosition(log.getLB());
        }


        lbLine.setStartY(30 + yLB);
        lbLine.setEndY(30 + yLB);
        situation.setText("Action : " + log.getSituation());
    }

}
