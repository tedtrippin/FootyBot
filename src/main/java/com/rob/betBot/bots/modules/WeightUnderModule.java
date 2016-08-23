package com.rob.betBot.bots.modules;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public class WeightUnderModule extends AbstractFilter {

    private static String desc = "Removes runners under a given weight.";

    WeightUnderModule() {
        super("5", "Remove runners under a certain weight", desc,
            new ModulePropertyImpl(PropertyNames.WEIGHT, ModulePropertyType.NUMBER, "Weight"));
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

    private double getWeightProperty() {
        return getPropertyAsDouble(PropertyNames.WEIGHT);
    }
}
