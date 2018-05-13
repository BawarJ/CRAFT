package bawar.craftv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This activity is responsible for displaying in-app help to guide the user in making the most
 * of the functionality of the application.
 * @author bawar
 * @version 1.0
 */
public class helpViewerActivity extends AppCompatActivity {

    /**
     * The onCreate method has been overridden but nothing special happens here in this case.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_viewer);
    }

    //TODO: Add some instructions into the Help Viewer (copy from User Manual)
}
