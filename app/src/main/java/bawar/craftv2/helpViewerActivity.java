package bawar.craftv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
        TextView helpText = findViewById(R.id.helpInfo);
        helpText.setText("Thank you for choosing to use CRAFT Cryptocurrency Regression Analysis " +
                "and Forecasting Tool to perform your analysis of cryptocurrency stocks.\n\n" +
                "At the moment, the app has the following features:\n" +
                "- Easy-to-use for an individual with basic understanding of regression analysis.\n" +
                "- Sleek and minimalistic user Interface for efficient data entry.\n" +
                "Regression analysis algorithm\n" +
                "- Linear Regression Model (Ordinary Least Squares).\n" +
                "- Exponential Regression Model.\n" +
                "Graphical plots\n" +
                "- Line graph joining data points.\n" +
                "- Regression line overlaid as separate line or curve.\n" +
                "Data retrieval\n" +
                "- Manual data entry into application.\n" +
                "- Imports data from saved csv files.\n" +
                "- Fetches real-time crypto-currency data using the AlphaVantage API to perform the analysis/forecasting on.\n" +
                "Equations \n" +
                "- Displays calculations used to make the predictions.\n" +
                "Exporting\n" +
                "- Exports graph to the PNG file type.\n\n" +
                "To use the application, simply select your chosen method of entering data " +
                "from the main menu.\n" +
                "Once this has been done, the corresponding data entry " +
                "menu will appear. Perform your required actions and finally click Plot Graph once " +
                "you are ready to see your data points plotted graphically.\n" +
                "At this point, you are able to scroll and zoom into the graph as you wish. You also " +
                "are given the option of entering Cursor Mode by toggling the bottom switch. Note " +
                "that it is not possible to scale or zoom the graph while in Cursor Mode. The Cursor Mode " +
                "displays the exact data that makes up the selected point. To show the corresponding regression " +
                "lines or curves, toggle the relevant switch.\n\n" +
                "If there are any more questions, please contact the developer at:\n" +
                "eeybj2@nottingham.ac.uk");
    }

    //TODO: Add some instructions into the Help Viewer (copy from User Manual)
}
