package com.rob.betBot.bots.modules;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public class AgeOverModule extends AbstractFilter {

    private static String desc = "Removes runner over a given weight.";

    AgeOverModule() {
        super("1", "Remove runners over a certain age", desc,
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

//            if (runner.getAge() >= getAgeProperty())
//                itr.remove();
//        }
    }

    public double getAgeProperty() {
        return getPropertyAsDouble(PropertyNames.AGE);
    }
}
