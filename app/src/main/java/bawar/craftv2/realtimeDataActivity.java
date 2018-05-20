package bawar.craftv2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * This class controls the scenario in the app that the data is imported from the AlphaVantage API.
 * @author bawar
 * @version 1.0
 */
public class realtimeDataActivity extends AppCompatActivity {

    ArrayList<DataPoint> dataPoints = new ArrayList<>();
    String coinSymbol = null;

    /**
     * The onCreate method is called when a new instance of the activity is created.
     * It is overridden and the plot graph button is greyed out until data is entered.
     * @param savedInstanceState stores data on activity to allow it to restore its state if user leaves and returns.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_data);

        findViewById(R.id.plotGraphButton).setClickable(false);
        findViewById(R.id.plotGraphButton).setAlpha(.5f);
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);


        Spinner coinSelectSpinner = findViewById(R.id.coinSelectSpinner);
        coinSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Bitcoin"))
                {
                    coinSymbol = "BTC";
                }
                if(selectedItem.equals("Ethereum"))
                {
                    coinSymbol = "ETH";
                }
                if(selectedItem.equals("Ripple"))
                {
                    coinSymbol = "XRP";
                }
                if(selectedItem.equals("Bitcoin Cash"))
                {
                    coinSymbol = "BCH";
                }
                if(selectedItem.equals("EOS"))
                {
                    coinSymbol = "EOS";
                }
                if(selectedItem.equals("Litecoin"))
                {
                    coinSymbol = "LTC";
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do something
            }
        });
    }

    /**
     * This method will use the pulled data from the web and add them to new rows so they
     * are visible within the app screen rather than just being saved as an array.
     */
    public void addDataRows() {

        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        findViewById(R.id.plotGraphButton).setClickable(true);
        findViewById(R.id.plotGraphButton).setAlpha(1f);

        TableLayout tl = (TableLayout) findViewById(R.id.tableLayout);

        TableRow.LayoutParams  params1=new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams params2=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
        TableLayout tbl=(TableLayout) findViewById(R.id.tableLayout);
        for(int i=0;i<dataPoints.size();i++)
        {
            //Creating new tablerows and textviews
            TableRow tr=new TableRow(this);
            TextView x = new TextView(this);
            TextView y = new TextView(this);
            //setting the text
            x.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(i).getX()));
            y.setText(String.format(Locale.ENGLISH, "%.3f", dataPoints.get(i).getY()));
            x.setLayoutParams(params1);
            y.setLayoutParams(params1);
            //the textviews have to be added to the row created
            tr.addView(x);
            tr.addView(y);
            tr.setLayoutParams(params2);
            tl.addView(tr);
        }

    }

    /**
     * This method will send the data through an intent to the next GraphActivity view
     * so that the data can be plotted on the graph.
     * In this case, there is some extra data passed through which checks if the
     * X axis should be rendered as dates or normal decimal values.
     * It also contains the coin symbol used so that it can be inserted into the graph title.
     * @param view the view that provides the context for this.
     */
    public void plotGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("DATA_POINTS", dataPoints);
        intent.putExtra("IS_DATE", true);
        intent.putExtra("COIN_SYMBOL", coinSymbol);
        startActivity(intent);
    }

    /**
     * This method will fetch the real-time data from the API. The coinSymbol variable will be used
     * to customise which coin data to retrieve.
     * @throws IOException an IO exception will be thrown if there is something wrong with the URL
     */
    public void fetchData() throws IOException {
        URL url = null;
        InputStream is = null;
        int count=0;

        try {
            String root = Environment.getExternalStorageDirectory().toString();

            //url = new URL("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_INTRADAY&symbol=BTC&market=GBP&apikey=41YYSI9MSFPQBN01&datatype=csv");
            url = new URL("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=" + coinSymbol + "&market=GBP&apikey=41YYSI9MSFPQBN01&datatype=csv");

            URLConnection con = url.openConnection();
            con.connect();

            is = new BufferedInputStream(url.openStream(), 8192);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuffer sb = new StringBuffer();

            //clear contents of list ready for fresh import
            dataPoints.clear();

            String line = "";
            br.readLine();//skip line 1
            while ((line = br.readLine()) != null) {
                sb.append(line);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(line.split(",")[0]);
                long time = date.getTime();
                dataPoints.add(new DataPoint(time,
                        Double.parseDouble(line.split(",")[1])));
            }

            Collections.reverse(dataPoints);  //X values need to be in ascending order so must reverse the arraylist

            br.close();

        } catch (Exception e) {
            Log.i("Data Retrieval: ", "Exception occurred: " + e.getMessage());
        }
        Log.i("Data Retrieval: ", "URL: " + url.toString());
    }

    /**
     * This method will begin the AsyncTask to download the data in the background
     * so as not to interfere with the main UI thread.
     * @param view the view that provides the context for this.
     */
    public void startDownload(View view) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        final DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute();
    }

    /** AsyncTask to download data */
    private class DownloadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try{
                fetchData();
            }catch(Exception e){
                Log.e("Background Task",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            addDataRows();
        }
    }

}

