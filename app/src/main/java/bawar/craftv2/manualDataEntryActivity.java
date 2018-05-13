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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class manualDataEntryActivity extends AppCompatActivity {

    ArrayList<DataPoint> dataPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data_entry);
    }

    public void addDataRow(View view) {
        //TODO: ADD ABILITY TO ADD NEW ROW
        //Intent intent = new Intent(this, manualDataEntryActivity.class);
        //startActivity(intent);

        EditText inputX = (EditText) findViewById(R.id.editTextX);
        EditText inputY = (EditText) findViewById(R.id.editTextY);

        if (inputX.toString().length()!=0 && inputX.toString().length()!=0) {

            dataPoints.add(new DataPoint(Double.valueOf(inputX.getText().toString()), Double.valueOf(inputY.getText().toString())));

            int lastAdded = dataPoints.size()-1;

            TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);
            //TODO: ALLOW CHANGES IN EDITTEXT TO UPDATE ARRAYLISTS AND GRAPHS
            //TODO: CHANGE NEW TEXTVIEW PARAMS TO NUMBER INPUT
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableLayout tbl = (TableLayout) findViewById(R.id.tableLayout);

            //Creating new tablerows and textviews
            TableRow tr = new TableRow(this);
            EditText x = new EditText(this);
            EditText y = new EditText(this);
            //setting the text
            x.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(lastAdded).getX()));
            y.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(lastAdded).getY()));
            x.setLayoutParams(params1);
            y.setLayoutParams(params1);
            //the textviews have to be added to the row created
            tr.addView(x);
            tr.addView(y);
            tr.setLayoutParams(params2);
            tl.addView(tr);
        }


        inputX.setText("");
        inputY.setText("");

    }

    public void plotGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("DATA_POINTS", dataPoints);
        startActivity(intent);
    }
}
