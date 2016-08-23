package com.rob.betBot.bots.modules;

import com.rob.betBot.Event;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.engine.BotEventTimeline;

public class AbstractFilter extends AbstractModule implements Filter {

    protected AbstractFilter(String id, String name, String description, ModuleProperty... properties) {
        super(id, name, description, properties);
    }

    @Override
    public void apply(Event race, BotEventTimeline timeline) {
        timesRun++;
    }
}
