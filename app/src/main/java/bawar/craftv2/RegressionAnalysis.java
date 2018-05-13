package bawar.craftv2;

import android.view.View;
import android.widget.Switch;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

/**
 * This class implements two different regression analysis algorithms, namely the
 * Linear Ordinary Least Squares (OLS) algorithm and the Exponential OLS algorithm.
 * There is
 * @author bawar
 * @version 1.0
 */
public class RegressionAnalysis {

    /**
     * This method will calculate the linear regression of the data and provide the data points
     * to allow this to be plotted.
     * <p>
     *     The sources used to help code this were:
     *     <i>https://www.varsitytutors.com/hotmath/hotmath_help/topics/line-of-best-fit</i> and
     *     <i>https://stackoverflow.com/questions/12946341/algorithm-for-scatter-plot-best-fit-line</i>
     * </p>
     * @param dataPoints list of DataPoints
     * @param toggle switch controlling the visibility of this line of best fit
     * @return The LineGraphSeries object, ready to add to the graph object in the calling method.
     */
    public LineGraphSeries<DataPoint> calculateLinearOLS(ArrayList<DataPoint> dataPoints, Switch toggle) {

        double sumX=.0, sumY=.0, meanX, meanY, m, c, sumXY=.0, sumXSquared=.0;
        int total = dataPoints.size();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<total;++i) {
            double X = dataPoints.get(i).getX();
            double Y = dataPoints.get(i).getY();
            sumX += X;
            sumY += Y;
            sumXSquared += X*X;
            sumXY += X*Y;
        }
        meanX = sumX/total;
        meanY = sumY/total;

        m = (sumXY / total - meanX * meanY) / (sumXSquared / total - meanX * meanX);
        c = (m * meanX-meanY);

        for (int i=0; i<dataPoints.size();++i) {
            double X = dataPoints.get(i).getX();
            double Y = m * X - c;

            series.appendData(new DataPoint(X, Y), true, dataPoints.size());
        }

        //show equation on switch
        toggle.append(": y="+String.format("%.3f",m)+"x+"+String.format("%.3f",c));

        return series;
    }

    /**
     * This method will calculate the exponential regression of the data and provide the data points
     * to allow this to be plotted.
     * <p>
     *     The source used to help code this were:
     *     <i>http://www.bragitoff.com/2015/09/c-program-for-exponential-fitting-least-squares/</i>
     * </p>
     * @param dataPoints list of DataPoints
     * @param toggle switch controlling the visibility of this curve of best fit
     * @return The LineGraphSeries object, ready to add to the graph object in the calling method.
     */
    public LineGraphSeries<DataPoint> calculateExponentialOLS(ArrayList<DataPoint> dataPoints, Switch toggle) {

        double sumX=.0, sumY=.0, a=.0, b=.0, c=.0, sumXY=.0, sumXSquared=.0;
        int total = dataPoints.size();
        double[] lnY = new double[total];

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<total;++i) {
            double X = dataPoints.get(i).getX();
            double Y = dataPoints.get(i).getY();

            lnY[i] = Math.log(Y);

            sumX += X;
            sumY += lnY[i];
            sumXSquared += X*X;
            sumXY += X*lnY[i];

        }

        a=(total*sumXY-sumX*sumY)/(total*sumXSquared-sumX*sumX);         //calculate slope(or the the power of exp)
        b=(sumXSquared*sumY-sumX*sumXY)/(sumXSquared*total-sumX*sumX);   //calculate intercept
        c = Math.exp(b);                                                 //since b=ln(c)

        for (int i=0; i<dataPoints.size();++i) {
            double X = dataPoints.get(i).getX();
            double Y = c*Math.exp(a * X);                    //to calculate y(fitted) at given x points
            series.appendData(new DataPoint(X, Y), true, dataPoints.size());
        }

        //show equation on switch
        toggle.append(": y="+String.format("%.3f",b)+"e^"+String.format("%.3f",a)+"x");

        return series;
    }

}
