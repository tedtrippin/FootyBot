package com.rob.betBot.bots.modules;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public class WeightOverModule extends AbstractFilter {

    private static String desc = "Removes runner over a given weight.";

    WeightOverModule() {
        super("4", "Remove runners over a certain weight", desc,
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

//            if (runner.getWeight() >= getWeightProperty())
//                itr.remove();
//        }
    }

    private double getWeightProperty() {
        return getPropertyAsDouble(PropertyNames.WEIGHT);
    }
}
