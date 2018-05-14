package bawar.craftv2;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class manualDataEntryActivity extends AppCompatActivity {

    ArrayList<DataPoint> dataPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data_entry);

        findViewById(R.id.plotGraphButton).setClickable(false);
        findViewById(R.id.plotGraphButton).setAlpha(.5f);
    }

    public void addDataRow(View view) {

        EditText inputX = (EditText) findViewById(R.id.editTextX);
        EditText inputY = (EditText) findViewById(R.id.editTextY);

        // first checks to make sure EditText boxes aren't empty
        if (inputX.getText().length()!=0 && inputY.getText().length()!=0) {
            // then checks that the dataPoints list actually has something inside
            if (dataPoints.size()>0) {
                // now checks that the entered x-value is greater than the previous one (to avoid errors in plotting the graph)
                if (Double.parseDouble(inputX.getText().toString()) > dataPoints.get(dataPoints.size()-1).getX()) {
                    dataPoints.add(new DataPoint(Double.valueOf(inputX.getText().toString()), Double.valueOf(inputY.getText().toString())));

                    int indexLastAdded = dataPoints.size() - 1;

                    TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
                    TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);

                    //Creating new tablerows and textviews
                    TableRow tr = new TableRow(this);
                    TextView x = new TextView(this);
                    TextView y = new TextView(this);
                    //setting the text
                    x.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(indexLastAdded).getX()));
                    y.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(indexLastAdded).getY()));
                    x.setLayoutParams(params1);
                    y.setLayoutParams(params1);
                    //the textviews have to be added to the row created
                    tr.addView(x);
                    tr.addView(y);
                    tr.setLayoutParams(params2);
                    tl.addView(tr);
                } else {
                    Toast.makeText(getApplicationContext(), "Make sure entered x-value is greater than the previous one!", Toast.LENGTH_SHORT).show();
                }
            } else {
                dataPoints.add(new DataPoint(Double.valueOf(inputX.getText().toString()), Double.valueOf(inputY.getText().toString())));

                int indexLastAdded = dataPoints.size()-1;

                TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
                TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);

                //Creating new tablerows and textviews
                TableRow tr = new TableRow(this);
                TextView x = new TextView(this);
                TextView y = new TextView(this);
                //setting the text
                x.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(indexLastAdded).getX()));
                y.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(indexLastAdded).getY()));
                x.setLayoutParams(params1);
                y.setLayoutParams(params1);
                //the textviews have to be added to the row created
                tr.addView(x);
                tr.addView(y);
                tr.setLayoutParams(params2);
                tl.addView(tr);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Make sure boxes aren't empty!", Toast.LENGTH_SHORT).show();
        }

        inputX.setText("");
        inputY.setText("");
        inputX.requestFocus();

        if (dataPoints.size() >= 2) {
            findViewById(R.id.plotGraphButton).setClickable(true);
            findViewById(R.id.plotGraphButton).setAlpha(1f);
        }
    }

    public void plotGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("DATA_POINTS", dataPoints);
        startActivity(intent);
    }


}