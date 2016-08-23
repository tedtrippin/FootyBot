package com.rob.betBot.bots.modules;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.exception.InvalidDataException;
import com.rob.betBot.util.JsonUtils;

public class ModuleFactory {

    private static Logger log = Logger.getLogger(ModuleFactory.class);

    public Filter createFilter(String id, ModuleProperty[] properties)
        throws InvalidDataException {

        Filter module = ModuleManager.instance().getFilter(id);
        if (module == null)
            throw new InvalidDataException("Module ID not found[" + id + "]");

        Filter newModule;
        try {
            // Get the plain object
            Constructor<? extends Filter> constructor = module.getClass().getDeclaredConstructor(new Class<?>[0]);
            newModule = constructor.newInstance();
        } catch (Exception ex) {
            log.error("Couldnt instaniate module", ex);
            return null;
        }

        Map<String, String> propertyValueMap = new HashMap<>();
        for (ModuleProperty property : properties) {
            propertyValueMap.put(property.getName(), property.getValue());
        }

        // Now set the properties values on the new module
        for (ModuleProperty p : newModule.getProperties()) {

            ModulePropertyImpl property =  (ModulePropertyImpl) p;
            if (!propertyValueMap.containsKey(property.getName()))
                throw new InvalidDataException("No value found for property[" + property.getName() + "]");

            String value = propertyValueMap.get(property.getName());
            property.setValue(value);
        }

        return newModule;
    }

    public BetPlacer createBetPlacer(String id, ModuleProperty[] properties)
        throws InvalidDataException {

        BetPlacer newModule;
        try {
            // Get the plain object
            BetPlacer module = ModuleManager.instance().getBetPlacer(id);
            Constructor<? extends BetPlacer> constructor = module.getClass().getDeclaredConstructor(new Class<?>[0]);
            newModule = constructor.newInstance();
        } catch (Exception ex) {
            log.error("Couldnt instaniate module", ex);
            return null;
        }

        Map<String, String> propertyValueMap = new HashMap<>();
        for (ModuleProperty property : properties) {
            propertyValueMap.put(property.getName(), property.getValue());
        }

        // Now set the properties values on the new module
        for (ModuleProperty p : newModule.getProperties()) {

            ModulePropertyImpl property =  (ModulePropertyImpl) p;
            if (!propertyValueMap.containsKey(property.getName()))
                throw new InvalidDataException("No value found for property[" + property.getName() + "]");

            String value = propertyValueMap.get(property.getName());
            property.setValue(value);
        }

        return newModule;
    }

    public Filter createFilter(JsonObject filterJson)
        throws InvalidDataException {

        try {
            String id = filterJson.get("id").getAsString();
            Filter filter = ModuleManager.instance().getFilter(id);
            JsonArray propertiesJson = filterJson.getAsJsonArray("properties");
            setValues(filter, propertiesJson);

            return filter;

        } catch (Exception ex) {
            log.error("Can't parse Filter JSON", ex);
            throw new InvalidDataException("Can't parse Filter JSON", ex);
        }
    }

    public BetPlacer createBetPlacer(JsonObject betPlacerJson)
        throws InvalidDataException {

        try {
            String id = betPlacerJson.get("id").getAsString();
            BetPlacer betPlacer = ModuleManager.instance().getBetPlacer(id);
            return JsonUtils.readValue(betPlacerJson, betPlacer.getClass());
        } catch (Exception ex) {
            log.error("Can't parse BetPlacer JSON", ex);
            throw new InvalidDataException("Can't parse BetPlacer JSON", ex);
        }
    }

    public String toJsonString(Bot bot) {

        try {
            return JsonUtils.toJsonString(bot);
        } catch (Exception ex) {
            log.error("Couldnt convert[" + bot.getClass() + "] to JSON", ex);
            return "";
        }
    }

    private void setValues(Filter filter, JsonArray propertiesJson) {

        Map<String, ModuleProperty> propertyMap = new HashMap<>();

        for (ModuleProperty property : filter.getProperties()) {
            propertyMap.put(property.getName(), property);
        }

        for (JsonElement j : propertiesJson) {
            JsonObject json = j.getAsJsonObject();
            String name = json.get("name").getAsString();
            String value = json.get("value").getAsString();

            if (!propertyMap.containsKey(name))
                continue;

            propertyMap.get(name).setValue(value);
        }
    }
}
