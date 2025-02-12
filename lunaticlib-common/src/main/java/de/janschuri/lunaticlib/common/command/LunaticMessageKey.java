package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.MessageKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunaticMessageKey implements MessageKey {

    private final String key;
    private final Map<String, String> defaultMessages = new HashMap<>();
    private final List<LunaticPlaceholder> placeholders = new ArrayList<>();
    private boolean withPrefix = true;

    public LunaticMessageKey(String key) {
        this.key = key;
    }

    public LunaticMessageKey defaultMessage(String defaultMessage) {
        return defaultMessage("en", defaultMessage);
    }

    public String getDefaultMessage() {
        return getDefaultMessage("en");
    }

    public LunaticMessageKey defaultMessage(String lang, String defaultMessage) {
        defaultMessages.put(lang, defaultMessage);
        return this;
    }

    public String getDefaultMessage(String lang) {
        return defaultMessages.get(lang);
    }

    public LunaticMessageKey noPrefix() {
        this.withPrefix = false;
        return this;
    }

    public LunaticMessageKey withPrefix() {
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
