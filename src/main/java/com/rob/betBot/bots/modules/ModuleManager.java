package com.rob.betBot.bots.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.bots.betPlacers.PercentageCorrectScoreLayPlacer;
import com.rob.betBot.bots.betPlacers.PredictiveBetPlacer;

public class ModuleManager {

    private static ModuleManager instance = new ModuleManager();

    private final Map<String, Filter> filterMap = new HashMap<>();
    private final Collection<String> filterDisplayNames = new ArrayList<>();
    private final Map<String, BetPlacer> betPlacerMap = new HashMap<>();
    private final Collection<String> betPlacerDisplayNames = new ArrayList<>();

    private ModuleManager() {

        for (Filter filter : getFilters()) {
            if (filterMap.containsKey(filter.getId()))
                throw new RuntimeException("ModuleManager contains duplicate Filter ID[" + filter.getId() + "]");

            filterMap.put(filter.getId(), filter);
            filterDisplayNames.add(filter.getName());
        }

        for (BetPlacer betPlacer : getBetPlacers()) {
            if (betPlacerMap.containsKey(betPlacer.getId()))
                throw new RuntimeException("ModuleManager contains duplicate BetPlacer ID[" + betPlacer.getId() + "]");

            betPlacerMap.put(betPlacer.getId(), betPlacer);
            betPlacerDisplayNames.add(betPlacer.getName());
        }
    }

    public static ModuleManager instance() {

        if (instance == null)
            instance = new ModuleManager();

        return instance;
    }

    public String[] getFilterNames() {
        return filterDisplayNames.toArray(new String[filterDisplayNames.size()]);
    }

    public Filter getFilter(String id) {
        return filterMap.get(id);
    }

    public String[] getBetPlacerNames() {
        return betPlacerDisplayNames.toArray(new String[betPlacerDisplayNames.size()]);
    }

    public BetPlacer getBetPlacer(String id) {
        return betPlacerMap.get(id);
    }

    public Filter[] getFilters() {
        return new Filter[0];
/*
        return new Filter[] {
            new AgeOverModule(),
            new AgeUnderModule(),
            new EventStartRemoveHighestOdds(),
            new WeightOverModule(),
            new WeightUnderModule()
            };
*/
    }

    public BetPlacer[] getBetPlacers() {
        return new BetPlacer[] {
            new PercentageCorrectScoreLayPlacer(),
            new PredictiveBetPlacer()
        };
    }
}
