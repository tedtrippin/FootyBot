package com.rob.betBot;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.rob.betBot.model.PricesData;
import com.rob.betBot.model.RunnerData;


/**
 * Results from 19:40 Wolverhampton, 30/10/2015
 * 1st  runner[8804566] Russian Radiance predicted[5.3000] prices 1,2,3[1.84, 1.23, 1.1]
 * 2nd  runner[8902729] Seychelloise     predicted[707.2 ] prices 1,2,3[3.8, 9.4, 40.0]
 * 3rd  runner[8545939] Anastazia        predicted[2172.0] prices 1,2,3[6.0, 25.0, 120.0]
 * 4th  runner[8775347] Exoplanet Blue   predicted[-7040.] prices 1,2,3[330.0, 1000.0, 1000.0]
 * 5th  runner[8778918] Rememberance Day predicted[5780.0] prices 1,2,3[280.0, 280.0, 500.0]
 * 6th  runner[8866659] Lady Estella     predicted[10916.] prices 1,2,3[48.0, 60.0, 500.0]
 * 7th  runner[5860966] Available        predicted[15100.] prices 1,2,3[200.0, 50.0, 580.0]
 * 8th  runner[9077530] Period Piece     predicted[786.43] prices 1,2,3[1.37, 55.0, 110.0]
 * 9th  runner[8770871] Poyle Jessica    predicted[-6440.] prices 1,2,3[380.0, 1000.0, 1000.0]
 * 10th runner[8844909] Edge of Love     predicted[5320.0] prices 1,2,3[700.0, 780.0, 1000.0]
 * 11th runner[8492629] Lady Desire      predicted[1000.0] prices 1,2,3[1000.0, 1000.0, 1000.0]
 * 12th runner[6721868] Light Rose       predicted[1000.0] prices 1,2,3[1000.0, 1000.0, 1000.0]
 *
 * @author robert.haycock
 *
 */
public class PricePredictorTest {

    private Market market;
    private List<MarketPrices> prices;

    @Test
    public void test() {

        PricePredictor predictor = new PricePredictor();
        PricesHelper predictedPrices = predictor.predict(market, 10000, prices);
        long[] sortedRunnerIds = predictedPrices.getSortedRunners();
        for (long runnerId : sortedRunnerIds) {
            System.out.println(runnerId + " - " + predictedPrices.getPrice(runnerId));
        }
    }

    @Before
    public void setup() {

        Collection<Runner> runners = new ArrayList<>();
        runners.add(new Runner(new RunnerData(1, 1, 1, "Russian Radiance")));
        runners.add(new Runner(new RunnerData(2, 1, 2, "Seychelloise")));
        runners.add(new Runner(new RunnerData(3, 1, 3, "Anastazia")));
        runners.add(new Runner(new RunnerData(4, 1, 4, "Exoplanet Blue")));
        runners.add(new Runner(new RunnerData(5, 1, 5, "Rememberance Day")));
        runners.add(new Runner(new RunnerData(6, 1, 6, "Lady Estella")));
        runners.add(new Runner(new RunnerData(7, 1, 7, "Available")));
        runners.add(new Runner(new RunnerData(8, 1, 8, "Period Piece")));
        runners.add(new Runner(new RunnerData(9, 1, 9, "Poyle Jessica")));
        runners.add(new Runner(new RunnerData(10, 1, 10, "Edge of Love")));
        runners.add(new Runner(new RunnerData(11, 1, 11, "Lady Desire")));
        runners.add(new Runner(new RunnerData(12, 1, 12, "Light Rose")));

        market = mock(Market.class);
        when(market.getRunners()).thenReturn(runners);

        Map<Long, Double> dataMap3 = new HashMap<>();
        dataMap3.put(1l, 1.1);
        dataMap3.put(2l, 40.0);
        dataMap3.put(3l, 120.0);
        dataMap3.put(4l, 1000.0);
        dataMap3.put(5l, 500.0);
        dataMap3.put(6l, 500.0);
        dataMap3.put(7l, 580.0);
        dataMap3.put(8l, 110.0);
        dataMap3.put(9l, 1000.0);
        dataMap3.put(10l, 1000.0);
        dataMap3.put(11l, 1000.0);
        dataMap3.put(12l, 1000.0);
        PricesData data3 = new PricesData(1, 1, 1, 300, dataMap3);
        MarketPrices prices3 = new MarketPrices(data3);

        Map<Long, Double> dataMap2 = new HashMap<>();
        dataMap2.put(1l, 1.23);
        dataMap2.put(2l, 9.4);
        dataMap2.put(3l, 25.0);
        dataMap2.put(4l, 1000.0);
        dataMap2.put(5l, 280.0);
        dataMap2.put(6l, 60.0);
        dataMap2.put(7l, 50.0);
        dataMap2.put(8l, 55.0);
        dataMap2.put(9l, 1000.0);
        dataMap2.put(10l, 780.0);
        dataMap2.put(11l, 1000.0);
        dataMap2.put(12l, 1000.0);
        PricesData data2 = new PricesData(1, 1, 1, 300, dataMap2);
        MarketPrices prices2 = new MarketPrices(data2);

        Map<Long, Double> dataMap1 = new HashMap<>();
        dataMap1.put(1l, 1.84);
        dataMap1.put(2l, 3.8);
        dataMap1.put(3l, 6.0);
        dataMap1.put(4l, 330.0);
        dataMap1.put(5l, 280.0);
        dataMap1.put(6l, 48.0);
        dataMap1.put(7l, 200.0);
        dataMap1.put(8l, 1.37);
        dataMap1.put(9l, 380.0);
        dataMap1.put(10l, 700.0);
        dataMap1.put(11l, 1000.0);
        dataMap1.put(12l, 1000.0);
        PricesData data1 = new PricesData(1, 1, 1, 300, dataMap1);
        MarketPrices prices1 = new MarketPrices(data1);

        prices = new ArrayList<>();
        prices.add(prices1);
        prices.add(prices2);
        prices.add(prices3);
    }
}
