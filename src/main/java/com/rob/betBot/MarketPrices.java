package com.rob.betBot;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.rob.betBot.model.PricesData;

public class MarketPrices {

    private PricesData pricesData;
    private List<Entry<Long, Double>> sortedPrices;
    private long[] sortedRunners;

    /**
     *
     * @param pricesData
     */
    public MarketPrices(PricesData pricesData) {

        this.pricesData = pricesData;

        init();
    }

    /**
     * Returns the id of the favourite, ie. the one with the lowest price.
     *
     * @return
     */
    public long getFavouriteId() {
        return sortedRunners[0];
    }

    /**
     * Gets the price for the requested runner.
     *
     * @param id
     * @return
     */
    public double getPrice(long id) {

        Long idLong = new Long(id);
        if (!pricesData.getPricesMap().containsKey(idLong))
            return 0;

        return pricesData.getPricesMap().get(idLong).doubleValue();
    }

    /**
     * returns the runners in order of price, lowest price first.
     *
     * @return
     */
    public long[] getSortedRunners() {
        return sortedRunners;
    }

    /**
     * Gets the price data.
     *
     * @return
     */
    public PricesData getPricesData() {
        return pricesData;
    }

    protected void init() {

        Ordering<Entry<Long, Double>> priceOrdering = Ordering.natural().onResultOf(
            new Function<Entry<Long, Double>, Double>() {
                @Override
                public Double apply(Entry<Long, Double> entry) {
                    return entry.getValue().doubleValue();
                }
            }
        );
        sortedPrices = priceOrdering.sortedCopy(pricesData.getPricesMap().entrySet());

        sortedRunners = new long[sortedPrices.size()];
        int idx = 0;
        for (Entry<Long, Double> entry : sortedPrices) {
            sortedRunners[idx++] = entry.getKey().longValue();
        }
    }
}
