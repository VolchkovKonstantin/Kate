package by.bsu.mag.vis;

import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
public class CustomStackedBarChart extends StackedBarChart<String, Number> {

    public CustomStackedBarChart( CategoryAxis xAxis, NumberAxis yAxis ) {
        super( xAxis, yAxis );
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();

        for ( Series<String, Number> series : getData() ) {
            for ( Data<String, Number> data : series.getData() ) {
                StackPane bar = (StackPane) data.getNode();

                Label label = null;

                for ( Node node : bar.getChildrenUnmodifiable() ) {
                    if ( node instanceof Label ) {
                        label = (Label) node;
                        break;
                    }
                }

                if ( label == null ) {
                    label = new Label( series.getName() );
                    bar.getChildren().add( label );
                }
                else {
                    label.setText( String.valueOf(data.getYValue().doubleValue()));
                }
            }
        }
    }
}