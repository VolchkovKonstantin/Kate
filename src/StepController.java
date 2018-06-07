package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
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

    private static int stepNumber = 0;

    private static int MAX_STEP = Algo.history.size() - 1;


    @FXML
    protected void prevAction(ActionEvent event) {
        if (stepNumber == 1) {
            prevBtn.setDisable(true);
        }
        if (stepNumber > 0) {
            stepNumber--;
        }
        if (stepNumber == MAX_STEP) {
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
        if (stepNumber == MAX_STEP) {
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
            stepNumber = MAX_STEP + 1;
        render(MAX_STEP);
    }

    @FXML
    protected void drawLine(ActionEvent event) {
        lbLine.setStartY(30 + hist.getYAxis().getDisplayPosition(80));
        lbLine.setEndY(30 + hist.getYAxis().getDisplayPosition(80));
    }

    private void render(int step) {
        step --;
        hist.setAnimated(false);
        Log log = Algo.history.get(step);
        ObservableList li = hist.getData();

        List<XYChart.Series> xis = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            xis.add(new XYChart.Series());
        }

        List<List<Integer>> nCup = log.getNCup();
        for (int j = 0; j < nCup.size(); j++) {
            List<Integer> works = nCup.get(j);
            for (int i = 0; i < works.size(); i++) {
                Integer numberWork = works.get(i);
                xis.get(i).getData().add(new XYChart.Data("P" + j, Algo.T[numberWork][j]));
            }
        }
        li.setAll(xis);
        hist.applyCss();
        hist.getParent().layout();

        lbLine.setStartY(30 + hist.getYAxis().getDisplayPosition(log.getLB()));
        lbLine.setEndY(30 + hist.getYAxis().getDisplayPosition(log.getLB()));
    }

}
