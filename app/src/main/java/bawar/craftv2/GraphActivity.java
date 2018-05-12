package bawar.craftv2;

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

        ArrayList<Double> entriesX = (ArrayList<Double>) getIntent().getSerializableExtra("ENTRIES_X");
        ArrayList<Double> entriesY = (ArrayList<Double>) getIntent().getSerializableExtra("ENTRIES_Y");

        final GraphView graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<entriesX.size();++i) {
            series.appendData(new DataPoint(entriesX.get(i), entriesY.get(i)), true, entriesX.size());
        }
        graph.addSeries(series);

        final LineGraphSeries<DataPoint> seriesOLS = new RegressionAnalysis().calculateOLS(entriesX, entriesY);

        Switch toggleOLS = findViewById(R.id.toggleViewOLS);

        toggleOLS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: MAKE THE COLOUR OF THIS SWITCH THE SAME AS THE COLOUR OF THE LINE OF BEST FIT
                if (isChecked) {
                    //TRUE - VISIBLE - Show OLS fitting
                    graph.addSeries(seriesOLS);

                } else {
                    //FALSE - NOT VISIBLE - Hide OLS fitting
                    graph.removeSeries(seriesOLS);
                }
            }
        });
    }

}
