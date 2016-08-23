package com.rob.betBot.mvc.model;

import java.util.Collection;

import com.rob.betBot.Runner;
import com.rob.betBot.engine.events.BotTimelineEvent;
import com.rob.betBot.engine.events.RunnerRemovedEvent;
import com.rob.betBot.model.RunnerData;

public class EventDescriptionFactory {

    private Collection<Runner> runners;

    public EventDescriptionFactory(Collection<Runner> runners) {
        this.runners = runners;
    }

    public String getDescription(BotTimelineEvent event) {
        switch (event.getEventType()) {
            case RUNNER_REMOVED_ODDS_TOO_HIGH :
                return getDescription((RunnerRemovedEvent)event);
            default :
                return "dunno";
        }
    }

    public String getDescription(RunnerRemovedEvent event) {

        RunnerData runner = getRunner(event.getRunnerId());
        StringBuilder sb = new StringBuilder(runner.getRunnerName())
            .append(" removed(price ").append(event.getPrice()).append(")");
        return sb.toString();
    }

    private RunnerData getRunner(long runnerId) {

        for (Runner runner : runners) {
            if (runner.getRunnerData().getExchangeRunnerId() == runnerId)
                return runner.getRunnerData();
        }

        return null;
    }
}
