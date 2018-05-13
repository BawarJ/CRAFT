package bawar.craftv2;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ArrayList<DataPoint> dataPoints = (ArrayList<DataPoint>) getIntent().getSerializableExtra("DATA_POINTS");

        //TODO: FOR DATES SET THE FOLLOWING graph.getGridLabelRenderer().setHumanRounding(false);

        final GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<dataPoints.size();++i) {
            series.appendData(dataPoints.get(i), true, dataPoints.size());
        }
        graph.addSeries(series);


        Switch toggleLinear = findViewById(R.id.toggleViewLinearOLS);
        final LineGraphSeries<DataPoint> seriesLinear = new RegressionAnalysis().calculateLinearOLS(dataPoints, toggleLinear);
        seriesLinear.setColor(Color.RED);

        toggleLinear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: MAKE THE COLOUR OF THIS SWITCH THE SAME AS THE COLOUR OF THE LINE OF BEST FIT
                if (isChecked) {
                    //TRUE - VISIBLE - Show OLS fitting
                    graph.addSeries(seriesLinear);

                } else {
                    //FALSE - NOT VISIBLE - Hide OLS fitting
                    graph.removeSeries(seriesLinear);
                }
            }
        });


        Switch toggleExponential = findViewById(R.id.toggleViewExponentialOLS);
        final LineGraphSeries<DataPoint> seriesExponential = new RegressionAnalysis().calculateExponentialOLS(dataPoints, toggleExponential);
        seriesExponential.setColor(Color.GREEN);

        toggleExponential.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: MAKE THE COLOUR OF THIS SWITCH THE SAME AS THE COLOUR OF THE LINE OF BEST FIT
                if (isChecked) {
                    //TRUE - VISIBLE - Show OLS fitting
                    graph.addSeries(seriesExponential);

                } else {
                    //FALSE - NOT VISIBLE - Hide OLS fitting
                    graph.removeSeries(seriesExponential);
                }
            }
        });
    }

}
