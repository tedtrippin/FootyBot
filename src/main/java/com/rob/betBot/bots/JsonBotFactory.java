package com.rob.betBot.bots;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.util.JsonUtils;

public class JsonBotFactory {

    private static Logger log = LogManager.getLogger(JsonBotFactory.class);
//    private static Map<Integer, Constructor<Filter>> filters = new HashMap<Integer, Constructor<Filter>>();;
//    private static Map<Integer, Constructor<BetPlacer>> betPlacers = new HashMap<Integer, Constructor<BetPlacer>>();;
//    private static Map<Integer, Class<? extends Filter>> filters = new HashMap<>();;
//    private static Map<Integer, Class<? extends BetPlacer>> betPlacers = new HashMap<>();;
//    private static final String IN_PLAY_FILTERS = "inPlayFilters";
//    private static final String PRE_PLAY_FILTERS = "prePlayFilters";
//    private static final String BET_PLACERS = "betPlacers";
//    private static final String ID = "id";

    public static Bot createBot(String botJson)
        throws InvalidBotException {

        try {
            return JsonUtils.readValue(botJson, Bot.class);
        } catch (Exception ex) {
            log.warn("JSON is invalid bot", ex);
            throw new InvalidBotException("JSON is invalid bot", ex);
        }
    }

//
//    static {
//        try {
//            // Load filter
//            log.info("Loading Filters...");
//            Reflections reflections = new Reflections("com.rob.betBot.bots.filters");
//            Set<Class<? extends Filter>> allFilters = reflections.getSubTypesOf(Filter.class);
//            for (Class<? extends Filter> filterClass : allFilters) {
//                log.info("  loading[" + filterClass.getSimpleName() + "]");
//                Field idField = filterClass.getDeclaredField("id");
//                Integer id = (Integer) idField.get(null);
//                filters.put(id, filterClass);
//            }
//
//            log.info("Loading BetPlacers...");
//            reflections = new Reflections("com.rob.betBot.bots.betPlacers");
//            Set<Class<? extends BetPlacer>> allBetPlacers = reflections.getSubTypesOf(BetPlacer.class);
//            for (Class<? extends BetPlacer> betPlacerClass : allBetPlacers) {
//                log.info("  loading[" + betPlacerClass.getSimpleName() + "]");
//                Field idField = betPlacerClass.getDeclaredField("id");
//                Integer id = (Integer) idField.get(null);
//                betPlacers.put(id, betPlacerClass);
//            }
//
//        } catch (Exception ex) {
//            log.fatal("Couldnt load Filter's/BetPlacer's", ex);
//        }
//    }
//
//    public static Bot createBot(String botJson)
//        throws InvalidBotException {
//
//        try {
//
//            List<Filter> prePlayFilters = new LinkedList<Filter>();
//            List<Filter> inPlayFilters = new LinkedList<Filter>();
//            List<BetPlacer> betPlacers = new LinkedList<BetPlacer>();
//
//            JSONObject json = new JSONObject(botJson);
//            if (json.has(PRE_PLAY_FILTERS)) {
//                JSONArray prePlayFiltersJson = json.getJSONArray(PRE_PLAY_FILTERS);
//                for (int idx = 0; idx < prePlayFiltersJson.length(); idx++) {
//
//                    JSONObject jsonObject = prePlayFiltersJson.getJSONObject(idx);
//
//                    int id = -1;
//
//                    Iterator<String> keys = jsonObject.keys();
//                    while (keys.hasNext()) {
//                        String key = keys.next();
//                        if (!key.equals(ID))
//                            continue;
//
//                        id = (int) jsonObject.getLong(key);
//                        Class<? extends Filter> filterClass = filters.get(id);
//                        Filter filter = objectMapper.readValue(jsonObject, filterClass);
//                        prePlayFilters.add(filter);
//
//                        break;
//                    }
//
//                }
//            }
//
//            if (json.has(IN_PLAY_FILTERS)) {
//                JSONArray inPlayFiltersJson = json.getJSONArray(IN_PLAY_FILTERS);
//                for (int idx = 0; idx < inPlayFiltersJson.length(); idx++) {
//
//                    JSONObject jsonObject = inPlayFiltersJson.getJSONObject(idx);
//
//                    int id = -1;
//                    Properties properties = new Properties();
//
//                    Iterator<String> keys = jsonObject.keys();
//                    while (keys.hasNext()) {
//                        String key = keys.next();
//                        if (key.equals(ID)) {
//                            id = (int) jsonObject.getLong(key);
//                        } else {
//                            properties.put(keys, jsonObject.getString(key));
//                        }
//                    }
//
//                    Filter filter = filters.get(id).newInstance(properties);
//                    inPlayFilters.add(filter);
//                }
//
//                if (json.has(BET_PLACERS)) {
//                    JSONArray betPlacersJson = json.getJSONArray(IN_PLAY_FILTERS);
//                    for (int idx = 0; idx < betPlacersJson.length(); idx++) {
//
//                        JSONObject jsonObject = betPlacersJson.getJSONObject(idx);
//
//                        int id = -1;
//                        Properties properties = new Properties();
//
//                        Iterator<String> keys = jsonObject.keys();
//                        while (keys.hasNext()) {
//                            String key = keys.next();
//                            if (key.equals(ID)) {
//                                id = (int) jsonObject.getLong(key);
//                            } else {
//                                properties.put(keys, jsonObject.getString(key));
//                            }
//                        }
//
//                        BetPlacer betPlacer = betPlacers.get(id).newInstance(properties);
//                        BetPlacers.add(BetPlacer);
//                    }
//            }
//        } catch (Exception ex) {
//            log.error("Bad bot JSON", ex);
//            throw new InvalidBotException(botJson, ex);
//        }
//        return null;
//    }
}
