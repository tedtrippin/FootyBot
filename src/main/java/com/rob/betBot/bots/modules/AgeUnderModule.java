package com.rob.betBot.bots.modules;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public class AgeUnderModule extends AbstractFilter {

    private static String desc = "Removes runners under a given age.";

    AgeUnderModule() {
        super("2", "Remove runners under a certain age", desc,
            new ModulePropertyImpl(PropertyNames.AGE, ModulePropertyType.NUMBER, "Age"));
    }

    @Override
    public void apply(Event race, BotEventTimeline timeline) {

        super.apply(race, timeline);

        if (timesRun > 0)
            return;

//        Iterator<Runner> itr = runners.iterator();
//        while(itr.hasNext()) {
//            Runner runner = itr.next();

//            if (runner.getWeight() <= getWeightProperty())
//                itr.remove();
//        }
    }

    private double getAgeProperty() {
        return getPropertyAsDouble(PropertyNames.AGE);
    }
}
