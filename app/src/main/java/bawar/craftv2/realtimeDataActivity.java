package bawar.craftv2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class realtimeDataActivity extends AppCompatActivity {

    ArrayList<DataPoint> dataPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_data);

        final DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute();

        findViewById(R.id.plotGraphButton).setClickable(false);
        findViewById(R.id.plotGraphButton).setAlpha(.5f);
    }

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

    public void plotGraph(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        intent.putExtra("DATA_POINTS", dataPoints);
        intent.putExtra("IS_DATE", true);
        startActivity(intent);
    }

    public void fetchData() throws IOException {
        URL url = null;
        InputStream is = null;
        int count=0;

        try {
            String root = Environment.getExternalStorageDirectory().toString();

            //url = new URL("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_INTRADAY&symbol=BTC&market=GBP&apikey=41YYSI9MSFPQBN01&datatype=csv");
            url = new URL("https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_DAILY&symbol=BTC&market=GBP&apikey=41YYSI9MSFPQBN01&datatype=csv");

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

