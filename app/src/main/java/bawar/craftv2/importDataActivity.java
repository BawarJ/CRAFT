package bawar.craftv2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class importDataActivity extends AppCompatActivity {

    ArrayList<DataPoint> dataPoints = new ArrayList<>();

    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        performFileSearch();
    }

    public void openFileViewer(View view) {
        performFileSearch();
    }

    public void addDataRow() {

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
        startActivity(intent);
    }

    /**
     * Fires an intent to spin up the "file chooser" UI and select an image.
     */
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == importDataActivity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("Data Retrieval: ", "Uri: " + uri.toString());
                try {
                    readDataFromUri(uri);
                } catch (IOException e) {
                    Log.i("Data Retrieval: ", "IOException in reading from: " + uri.toString());
                }
                EditText filePath = (EditText) findViewById(R.id.editTextFilePath);
                filePath.setText(uri.getLastPathSegment().toString().split("/")[1]);

                addDataRow();
            }
        }
    }

    private String readDataFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        //clear contents of lists ready for fresh import
        dataPoints.clear();

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);

            dataPoints.add(new DataPoint(Double.parseDouble(line.split(",")[0]), Double.parseDouble(line.split(",")[1])));
        }

        //fileInputStream.close();
        //parcelFileDescriptor.close();
        return stringBuilder.toString();
    }
}
