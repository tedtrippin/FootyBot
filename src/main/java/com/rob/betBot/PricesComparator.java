package com.rob.betBot;

import java.util.Comparator;
import java.util.Map.Entry;

public class PricesComparator implements Comparator<Entry<Long, Double>> {

    public int compare(Entry<Long, Double> price1, Entry<Long, Double> price2) {

        // In contradiction to normal comparator we want the highest price first
        // so we're looking for runner1 to have a lower price.

        if (price1.getValue().doubleValue() > price2.getValue().doubleValue())
            return 1;

        if (price1.getValue().doubleValue() < price2.getValue().doubleValue())
            return -1;

        return 0;
    }
}
