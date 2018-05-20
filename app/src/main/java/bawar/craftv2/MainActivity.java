package bawar.craftv2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The main activity which shows the menu for navigating the application.
 * @author bawar
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The onCreate method has been overridden but nothing special happens here in this case.
     * @param savedInstanceState stores data on activity to allow it to restore its state if user leaves and returns.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts an intent to start a new activity to manually enter data.
     * @param view the view that provides the context for this.
     */
    public void manualDataEntry(View view) {
        Intent intent = new Intent(this, manualDataEntryActivity.class);
        startActivity(intent);
    }

    /**
     * Starts an intent to start a new activity to import data from a csv file.
     * @param view the view that provides the context for this.
     */
    public void importData(View view) {
        Intent intent = new Intent(this, importDataActivity.class);
        startActivity(intent);
    }

    /**
     * Starts an intent to start a new activity to fetch data from the web using the AlphaVantage API.
     * @param view the view that provides the context for this.
     */
    public void realtimeData(View view) {
        Intent intent = new Intent(this, realtimeDataActivity.class);
        startActivity(intent);
    }

    /**
     * Starts an intent to start a new activity to give help on using the app.
     * @param view the view that provides the context for this.
     */
    public void helpViewer(View view) {
        Intent intent = new Intent(this, helpViewerActivity.class);
        startActivity(intent);
    }
}
