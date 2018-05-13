package bawar.craftv2;

import android.view.View;
import android.widget.Switch;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class RegressionAnalysis {

    public LineGraphSeries<DataPoint> calculateLinearOLS(ArrayList<DataPoint> dataPoints, Switch toggle) {
        //calculate Linear OLS line of best fit
        //https://www.varsitytutors.com/hotmath/hotmath_help/topics/line-of-best-fit
        //https://stackoverflow.com/questions/12946341/algorithm-for-scatter-plot-best-fit-line
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

    public LineGraphSeries<DataPoint> calculateExponentialOLS(ArrayList<DataPoint> dataPoints, Switch toggle) {
        //calculate Exponential OLS line of best fit
        //http://www.bragitoff.com/2015/09/c-program-for-exponential-fitting-least-squares/
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



    //TODO: in report, mention that if I had more time I would also have added the Logarithmic plot
    //TODO: in report, mention that if I had more time I would also have added MOVING AVERAGES which are much more relevant to stock prices
}
