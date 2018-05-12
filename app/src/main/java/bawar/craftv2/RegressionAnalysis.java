package bawar.craftv2;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class RegressionAnalysis {

    public LineGraphSeries<DataPoint> calculateOLS(ArrayList<Double> X, ArrayList<Double> Y) {
        //calculate OLS line of best fit
        //https://www.varsitytutors.com/hotmath/hotmath_help/topics/line-of-best-fit
        double sumX = .0, sumY = .0, meanX, meanY, m, c, sumXY = .0, sumSquaredX = .0;
        int total = X.size();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i=0; i<total;++i) {
            sumX += X.get(i);
            sumY += Y.get(i);
        }
        meanX = sumX/total;
        meanY = sumY/total;

        m = (sumXY / total - meanX * meanY) / (sumSquaredX / total - meanX * meanX);
        c = (m * meanX-meanY);

        for (int i=0; i<X.size();++i) {
            double yValue = m * X.get(i) + c;

            series.appendData(new DataPoint(X.get(i), yValue), true, X.size());
        }
        return series;
    }
}
