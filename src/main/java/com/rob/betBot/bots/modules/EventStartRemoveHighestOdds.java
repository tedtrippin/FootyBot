package com.rob.betBot.bots.modules;

import org.apache.log4j.Logger;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public class EventStartRemoveHighestOdds extends AbstractFilter {

    private static final Logger log = Logger.getLogger(EventStartRemoveHighestOdds.class);

    private long lastRun = 0;
    private int numberRemoved = 0;

    private static String desc = "After each interval period has passed, Removes the runner with the highest odds. "
        + "You can set the interval period (in seconds) and the maximum number of runners and/or "
        + "how many to leave. For example 'Every 10 seconds remove a ruuner until 5 have been removed "
        + "or there are only 2 left'";

    EventStartRemoveHighestOdds() {
        super("3", "Remove highest odds", desc,
            new ModulePropertyImpl("interval", ModulePropertyType.NUMBER, "Interval (seconds)"),
            new ModulePropertyImpl("numberToRemove", ModulePropertyType.NUMBER, "Number of runners to remove"),
            new ModulePropertyImpl("numberToLeave", ModulePropertyType.NUMBER, "Number of runners to leave"));
    }

    @Override
    public void apply(Event event, BotEventTimeline timeline) {

        log.debug("Applying RaceStartRemoveHighestOdds");
/*
        long currentTime = timeline.getLatestPrices().getPricesData().getTimeMs();
        if (lastRun > 0 && (lastRun + getInterval()*1000) > currentTime)
            return;

        super.apply(event, runners, timeline);

        lastRun = currentTime;
        MarketPrices prices = event.getLatestPrices();

        int numberToRemove = getNumberToRemove();
        if (numberToRemove > 0 && numberRemoved >= numberToRemove)
            return;

        int numberToLeave = getNumberToLeave();
        if (numberToLeave > 0 && runners.size() <= numberToLeave)
            return;

        long[] runnerIds = prices.getSortedRunners();
        long highestOddRunnerId = runnerIds[runnerIds.length-1];

        Iterator<Runner> itr = runners.iterator();
        while(itr.hasNext()) {
            Runner runner = itr.next();
            if (runner.getId() != highestOddRunnerId)
                continue;

            itr.remove();
            numberRemoved++;

            double price = prices.getPrice(highestOddRunnerId);
            timeline.addEvent(new RunnerRemovedEvent( runner.getId(), price,
                EventTypeEnum.RUNNER_REMOVED_ODDS_TOO_HIGH, event.getLatestPrices().getPricesData().getTimeMs()));
            break;
        }
*/
    }

    private int getInterval() {
        return getPropertyAsInt("interval");
    }

    private int getNumberToRemove() {
        return getPropertyAsInt("numberToRemove");
    }

    private int getNumberToLeave() {
        return getPropertyAsInt("numberToLeave");
    }
}
