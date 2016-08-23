package com.rob.betBot.util;

import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.rob.betBot.bots.modules.ModuleProperty;
import com.rob.betBot.bots.modules.ModulePropertyImpl;
import com.rob.betBot.bots.modules.ModulePropertyType;

public class JsonUtils {

    private static final Gson gson;

    static {
        gson = new GsonBuilder()
            .registerTypeAdapter(ModuleProperty.class, new ModulePropertyAdapter())
            .create();
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T readValue(JsonObject json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T readValue(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }

    public static <T> T readValue(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    public static JsonElement toJsonElement(Object object) {
        return gson.toJsonTree(object);
    }

    public static String getString(String json, String field) {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        return jsonObject.get(field).toString();
    }

    public static JsonElement parse(String json) {

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json).getAsJsonObject();
        return jsonElement;
    }

    static class ModulePropertyAdapter implements JsonDeserializer<ModuleProperty>, JsonSerializer<ModuleProperty> {

        @Override
        public ModuleProperty deserialize(JsonElement j, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

            JsonObject json = (JsonObject)j;
            String name = json.get("name").getAsString();
            String displayName = json.get("displayName").getAsString();
            ModulePropertyType type = Enum.valueOf(ModulePropertyType.class, json.get("type").getAsString());
            String value = json.get("value").getAsString();

            ModulePropertyImpl property = new ModulePropertyImpl(name, type, displayName);
            property.setValue(value);

            return property;
        }

        @Override
        public JsonElement serialize(ModuleProperty property, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(property);
        }
    }
}
