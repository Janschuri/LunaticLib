package de.janschuri.lunaticlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageKey {

    private final String key;
    private final Map<String, String> defaultMessages = new HashMap<>();
    private final List<Placeholder> placeholders = new ArrayList<>();
    private boolean withPrefix = true;

    public MessageKey(String key) {
        this.key = key;
    }

    public MessageKey defaultMessage(String defaultMessage) {
        return defaultMessage("en", defaultMessage);
    }

    public String getDefaultMessage() {
        return getDefaultMessage("en");
    }

    public MessageKey defaultMessage(String lang, String defaultMessage) {
        defaultMessages.put(lang, defaultMessage);
        return this;
    }

    public String getDefaultMessage(String lang) {
        return defaultMessages.get(lang);
    }

    public MessageKey noPrefix() {
        this.withPrefix = false;
        return this;
    }

    public MessageKey withPrefix() {
        this.withPrefix = true;
        return this;
    }

    public boolean isWithPrefix() {
        return withPrefix;
    }

    @Override
    public String toString() {
        return "messages." + key;
    }
}
