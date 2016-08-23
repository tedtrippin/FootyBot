package com.rob.betBot;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PricesTest {

    private PricesHelper prices;

    @Before
    public void before() {
        prices = getTestPrices();
    }

    @Test
    public void testGetPrice() {
        assertEquals(prices.getPrice(1L), 3D, 0D);
    }

    @Test
    public void testGetSortedPrice() {
        assertEquals(prices.getSortedPrice(0), 1D, 0D);
        assertEquals(prices.getSortedPrice(1), 2D, 0D);
        assertEquals(prices.getSortedPrice(2), 3D, 0D);
        assertEquals(prices.getSortedPrice(3), 4D, 0D);
    }

    @Test
    public void testGetFavouriteId() {
        assertEquals(prices.getFavouriteId(), 4D, 0D);
    }

    @Test
    public void testGetSortedRunners() {
        assertEquals(prices.getSortedRunners()[0], 4L);
        assertEquals(prices.getSortedRunners()[1], 2L);
        assertEquals(prices.getSortedRunners()[2], 1L);
        assertEquals(prices.getSortedRunners()[3], 3L);
    }

    private PricesHelper getTestPrices() {

        Map<Long, Double> testPriceMap = new HashMap<>();
        testPriceMap.put(2L, 2D);
        testPriceMap.put(1L, 3D);
        testPriceMap.put(4L, 1D);
        testPriceMap.put(3L, 4D);

        return new PricesHelper(testPriceMap);
    }

}
