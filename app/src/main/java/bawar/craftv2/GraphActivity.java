package bawar.craftv2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Controls the creation of graphs using the GraphView library. This activity also contains
 * switches to activate or deactivate the corresponding lines of best fit.
 * @author bawar
 * @version 1.0
 */
public class GraphActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 38;

    /**
     * The onCreate method is called when a new instance of the activity is created.
     * It is overridden and code to identify the GraphView, create the graphs and
     * series, and to control the toggling of the lines of best fit are implemented in this method.
     * Anonymous inner class are used to listen for
     * any changes of state in the Switch objects.
     * @param savedInstanceState stores data on activity to allow it to restore its state if user leaves and returns.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ArrayList<DataPoint> dataPoints = (ArrayList<DataPoint>) getIntent().getSerializableExtra("DATA_POINTS");
        boolean isDate = (boolean) getIntent().getBooleanExtra("IS_DATE", false);
        String coinSymbol = (String) getIntent().getStringExtra("COIN_SYMBOL");

        final GraphView graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(45);
        graph.setBackgroundColor(Color.WHITE);

        //SET VIEW TO COVER WHOLE RANGE
        graph.getViewport().setMinX(dataPoints.get(0).getX());
        graph.getViewport().setMaxX(dataPoints.get(dataPoints.size()-1).getX());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling


        //format X and Y axes
        if (isDate) {

            graph.setTitle("Daily " + coinSymbol + " Value in GBP(£)");
            graph.getGridLabelRenderer().setVerticalAxisTitle("Price (GBP)");
            graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this) {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (!isValueX) {
                        // show currency for y values
                        return "£" + super.formatLabel(value, isValueX);
                    } else {
                        return super.formatLabel(value, isValueX);
                    }

                }
            });
            graph.getGridLabelRenderer().setHumanRounding(false);

        } else {
            graph.setTitle("Stock Prices in GBP(£)");
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX) {
                        // show normal x values
                        return super.formatLabel(value, isValueX);
                    } else {
                        // show currency for y values
                        return "£" + super.formatLabel(value, isValueX);
                    }
                }
            });
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<dataPoints.size();++i) {
            series.appendData(dataPoints.get(i), true, dataPoints.size());
        }
        series.setAnimated(true);
        graph.addSeries(series);

        Switch toggleLinear = findViewById(R.id.toggleViewLinearOLS);
        final LineGraphSeries<DataPoint> seriesLinear = new RegressionAnalysis().calculateLinearOLS(dataPoints, toggleLinear);
        seriesLinear.setColor(Color.RED);

        toggleLinear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                if (isChecked) {
                    //TRUE - VISIBLE - Show OLS fitting
                    graph.addSeries(seriesExponential);

                } else {
                    //FALSE - NOT VISIBLE - Hide OLS fitting
                    graph.removeSeries(seriesExponential);
                }
            }
        });

        Switch toggleCursorMode = findViewById(R.id.toggleViewCursorMode);
        toggleCursorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TRUE - Enable Cursor Mode and Disable Scrolling/Scaling
                    graph.setCursorMode(true);
                    graph.getViewport().setScalable(false); // disables horizontal zooming and scrolling
                } else {
                    //FALSE - Diable Cursor Mode and Enable Scrolling/Scaling
                    graph.setCursorMode(false);
                    graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                }
            }
        });
    }

    /**
     * This method provides the functionality to take a snapshot of the plotted graph and save it
     * to the user's device.
     */
    public void takeSnapshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/CRAFT/CRAFT-Snapshot-"+now+".png";

        // create bitmap screen capture
        //View v1 = getWindow().getDecorView().getRootView();
        View v1 = findViewById(R.id.graph);
        v1.setDrawingCacheEnabled(true);
        Bitmap image = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        File file = new File(path);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //make confirmation toast
        Toast.makeText(getApplicationContext(), "Saved to:" + path, Toast.LENGTH_LONG).show();

    }

    /**
     * This method will grant permissions to write to external storage. It will be used
     * to write the PNG snapshot of the graph to the device.
     * @param view the view that provides the context for this.
     */
    public void requestPermissions(View view) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            takeSnapshot();
        }
    }

    /**
     * This method is called when the result of the Request Permissions task is retrieved.
     * @param requestCode refers to the custom integer (38) that defines the requested permission.
     * @param permissions array of permissions.
     * @param grantResults the integer values of the granted results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    //  task you need to do.
                    takeSnapshot();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}


