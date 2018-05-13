package bawar.craftv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
//TODO: ADD JAVADOCS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void manualDataEntry(View view) {
        Intent intent = new Intent(this, manualDataEntryActivity.class);
        startActivity(intent);
    }

    public void importData(View view) {
        Intent intent = new Intent(this, importDataActivity.class);
        startActivity(intent);
    }

    public void realtimeData(View view) {
        Intent intent = new Intent(this, realtimeDataActivity.class);
        startActivity(intent);
    }

    public void helpViewer(View view) {
        Intent intent = new Intent(this, helpViewerActivity.class);
        startActivity(intent);
    }
}
