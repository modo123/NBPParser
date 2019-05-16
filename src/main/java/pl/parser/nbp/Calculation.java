package pl.parser.nbp;

import java.text.DecimalFormat;
import java.util.List;

public class Calculation
{
    static DecimalFormat decimalFormat = new DecimalFormat("#.####");

    public static String calculateAverageBuyingRate(List<Double> buyingRates)
    {
        double buyingRatesSum = 0;
        for (double buyingRate : buyingRates)
            buyingRatesSum += buyingRate;

        double averageBuyingRate = buyingRatesSum / buyingRates.size();

        return decimalFormat.format(averageBuyingRate);
    }

    public static String calculateSellingRatesStandardDeviation(List<Double> sellingRates)
    {
        double sellingRatesSum = 0;
        for (double sellingRate : sellingRates)
            sellingRatesSum += sellingRate;

        double averageSellingRate = sellingRatesSum / sellingRates.size();

        double variance = 0;
        for (double sellingRate : sellingRates)
            variance += Math.pow(sellingRate - averageSellingRate, 2);

        variance = variance / sellingRates.size();

        double standardDeviation = Math.sqrt(variance);

        return decimalFormat.format(standardDeviation);
    }
}
