package de.janschuri.lunaticlib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageKey {

    private String key;
    private String defaultMessage;
    private Map<String, String> placeholders = new HashMap<>();

    public MessageKey(String key) {
        this.key = key;
    }

    public MessageKey defaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public MessageKey placeholder(String key, String value) {
        placeholders.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "messages." + key;
    }
}
