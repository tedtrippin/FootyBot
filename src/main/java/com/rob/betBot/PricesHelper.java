package com.rob.betBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

public class PricesHelper {

    protected Map<Long, Double> pricesMap = new HashMap<Long, Double>();
    private List<Entry<Long, Double>> sortedPrices;
    private long[] sortedRunners;

    /**
     *
     * @param prices
     * @param timeMs The time the prices were received
     */
    public PricesHelper(Map<Long, Double> prices) {
        pricesMap = prices;
        init();
    }

    /**
     * Gets the price for the requested runner.
     *
     * @param id
     * @return
     */
    public double getPrice(long id) {

        Long idLong = new Long(id);
        if (!pricesMap.containsKey(idLong))
            return 0;

        return pricesMap.get(idLong).doubleValue();
    }

    /**
     * Returns the sorted predicted price at that index. So, getSortedPrice(0)
     * would return the price for the predicted favourite.
     *
     * @param idx
     * @return
     */
    public double getSortedPrice(int idx) {
        return sortedPrices.get(idx).getValue().doubleValue();
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
     * returns the runners in order of price, lowest price first.
     *
     * @return
     */
    public long[] getSortedRunners() {
        return sortedRunners;
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
        sortedPrices = priceOrdering.sortedCopy(pricesMap.entrySet());

        sortedRunners = new long[sortedPrices.size()];
        int idx = 0;
        for (Entry<Long, Double> entry : sortedPrices) {
            sortedRunners[idx++] = entry.getKey().longValue();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Prices[");

        boolean first = true;
        for (Entry<Long, Double> entry : pricesMap.entrySet()) {

            if (first)
                first = false;
            else
                sb.append(',');

            sb.append(entry.getKey()).append(',').append(entry.getValue());
        }
        sb.append(']');

        return sb.toString();
    }
}


