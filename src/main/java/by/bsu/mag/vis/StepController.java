package main.java.by.bsu.mag.vis;

import by.bsu.mag.algo.Algo;
import by.bsu.mag.algo.Log;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class StepController {

    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button resultBtn;
    @FXML
    private StackedBarChart<String, Number> hist;
    @FXML
    private Line lbLine;
    @FXML
    private Label situation;

    @FXML
    private NumberAxis numberAxis;

    private static int stepNumber = 0;

    private static int MAX_STEP = Algo.history.size() - 1;

    @FXML
    public void initialize() {
        render(0);
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

    private void render(int step) {
        hist.setAnimated(false);
        Log log = Algo.history.get(step);
        ObservableList li = hist.getData();

        List<XYChart.Series> xis = new ArrayList<>();
        for (int i = 0; i < Algo.MAX_K; i++) {
            xis.add(new XYChart.Series());
        }

        List<List<Integer>> nCup = log.getNCup();
        for (int j = 0; j < Algo.M; j++) {
            List<Integer> works = nCup.get(j);
            if (works.isEmpty()) {
                for (int i = 0; i < Algo.MAX_K; i++) {
                    xis.get(i).getData().add(new XYChart.Data("P" + j, 0));
                }
            }
            for (int i = 0; i < works.size(); i++) {
                Integer numberWork = works.get(i);
                xis.get(i).getData().add(new XYChart.Data("P" + j, Algo.T[numberWork][j]));
            }
        }
        li.setAll(xis);
        numberAxis.setAutoRanging(true);
        hist.applyCss();
        hist.getParent().layout();
        double yLB = hist.getYAxis().getDisplayPosition(log.getLB());
        if (yLB <= 0) {
            numberAxis.setAutoRanging(false);
            numberAxis.setUpperBound(Algo.history.get(0).getLB().doubleValue());
        } else {
            numberAxis.setAutoRanging(true);
        }
        hist.applyCss();
        hist.getParent().layout();

        yLB = hist.getYAxis().getDisplayPosition(log.getLB());

        lbLine.setStartY(30 + yLB);
        lbLine.setEndY(30 + yLB);
        situation.setText("Action : " + log.getSituation());
    }

}
