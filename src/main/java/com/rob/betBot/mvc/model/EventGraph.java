package com.rob.betBot.mvc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.Runner;
import com.rob.betBot.engine.BotEventTimeline;
import com.rob.betBot.engine.events.BotTimelineEvent;
import com.rob.betBot.engine.events.RunnerRemovedEvent;

public class EventGraph {

    private final int canvasHeight = 350;
    private final int canvasWidth;
    private final int graphHeight = 320;
    private final int graphWidth;
    private final int xPadding = 30;
    private final int yPadding = 10;
    private final String[] runnerPricesStrings;
    private final int yCanvasGap;
    private final List<String> yAxisPriceLabels;
    private final int xCanvasGap = 20;
    private final int numberOfXPoints;
    private final List<TimelineEvent> eventEvents;

    public EventGraph(BotEventTimeline timeline, Market market) {

        Collection<Runner> runners = market.getRunners();
        this.runnerPricesStrings = new String[runners.size()];

        eventEvents = new ArrayList<>();
        EventDescriptionFactory descFactory = new EventDescriptionFactory(runners);
        for (BotTimelineEvent raceEvent : timeline.getEvents()) {
            eventEvents.add(new TimelineEvent(raceEvent.getTimeOfEvent(), descFactory.getDescription(raceEvent)));
        }

        Set<Runner> copyOfRunners = new HashSet<>(runners);
        Map<Long, List<Double>> racePriceMap = new HashMap<>();
        double maxPrice = 0;
        Map<Long, RunnerRemovedEvent> runnerRemovedMap = getRunnerRemovedMap(timeline.getEvents());

        for (Runner runner: runners) {
            racePriceMap.put(runner.getRunnerData().getExchangeRunnerId(), new ArrayList<Double>());
        }

        // Build lists of quantifialised prices for each runner, stopping when they were removed from the running
        int count = 0;
        Iterator<MarketPrices> pricesIterator = market.getPrices().iterator();
        while (pricesIterator.hasNext()) {
            MarketPrices racePrices = pricesIterator.next();

            count++;

            Iterator<Runner> runnerItr = copyOfRunners.iterator();
            while (runnerItr.hasNext()) {

                Runner runner = runnerItr.next();
                long runnerId = runner.getRunnerData().getExchangeRunnerId();
                double price = racePrices.getPrice(runnerId);

                price = quantifalisePrice(price);

                if (price > maxPrice)
                    maxPrice = price;

                racePriceMap.get(runnerId).add(price);

                if (runnerRemovedMap.containsKey(runnerId)) {
                    if (runnerRemovedMap.get(runnerId).getTimeOfEvent() == racePrices.getPricesData().getTimeMs()) {
                        runnerItr.remove();
                    }
                }
            }

            if (copyOfRunners.isEmpty())
                break;
        }

        numberOfXPoints = count;
        graphWidth = xCanvasGap * numberOfXPoints;
        canvasWidth = graphWidth + 30;
        int maxYPoint = (int) Math.ceil(maxPrice);
        maxYPoint = Math.max(maxYPoint, 10); // Default to a min of 10

        // Build our y-axis points
        yAxisPriceLabels = new ArrayList<>();
        for (int idx = 0, step = 1, yPoint = 0; idx <= maxYPoint; idx++) {
            yAxisPriceLabels.add(String.valueOf(yPoint));

            yPoint += step;
            if (yPoint == 10)
                step = 10;
            else if (yPoint == 100)
                step = 100;
        }

        // Now we can calculate the gap between labels on Y-axis
        yCanvasGap = graphHeight / (yAxisPriceLabels.size() - 1);

        // Build the runners Y co-ordinate
        int runnerIdx = 0;
        int maxYcoord = 0;
        int yCoord = 0;
        for (Entry<Long, List<Double>> entry : racePriceMap.entrySet()) {

            List<Double> priceList = entry.getValue();
            long runnerId = entry.getKey();

            StringBuilder sb = new StringBuilder("[");
            for (Double d : priceList) {
                yCoord = (int) (d * yCanvasGap);

                if (yCoord > maxYcoord)
                    maxYcoord = yCoord;

                sb.append(yCoord).append(',');
            }

//            if (runnerRemovedMap.containsKey(runnerId)) {
//                RunnerRemovedEvent runnerRemovedEvent = runnerRemovedMap.get(runnerId);
//                sb.append("\"").append(getRunnerName(runnerId, runners)).append(" removed (price ")
//                    .append(runnerRemovedEvent.getPrice()).append(")\" ");
//            }

            sb.setLength(sb.length() - 1);
            sb.append("]");
            runnerPricesStrings[runnerIdx++] = sb.toString();
        }
        System.out.println("Done");
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getGraphHeight() {
        return graphHeight;
    }

    public int getGraphWidth() {
        return graphWidth;
    }

    public int getxPadding() {
        return xPadding;
    }

    public int getyPadding() {
        return yPadding;
    }

    public String[] getRunnerPricesStrings() {
        return runnerPricesStrings;
    }

    public int getyCanvasGap() {
        return yCanvasGap;
    }

    public List<String> getyAxisPriceStrings() {
        return yAxisPriceLabels;
    }

    public int getxCanvasGap() {
        return xCanvasGap;
    }

    public int getNumberOfXPoints() {
        return numberOfXPoints;
    }

    public List<TimelineEvent> getEventEvents() {
        return eventEvents;
    }

    private Map<Long, RunnerRemovedEvent> getRunnerRemovedMap(List<BotTimelineEvent> events) {

        Map<Long, RunnerRemovedEvent> runnerRemovedMap = new HashMap<>();
        for (BotTimelineEvent e : events) {
            if (!(e instanceof RunnerRemovedEvent))
                continue;

            RunnerRemovedEvent event = (RunnerRemovedEvent)e;
            runnerRemovedMap.put(event.getRunnerId(), event);
        }

        return runnerRemovedMap;
    }

    /**
     * Quantifalis-a-lises the price. Assumes a mmx price of 1000.
     * Converts price to between 0-30.
     *
     * @param price
     * @return
     */
    private double quantifalisePrice(double price) {

        if (price <= 10)
            return price;

        if (price <= 100)
            return price/10 + 10;

        return price/100 + 20;
    }
}
