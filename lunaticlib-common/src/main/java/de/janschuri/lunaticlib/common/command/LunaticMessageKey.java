package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.ConfigKey;
import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.common.config.LunaticConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunaticMessageKey extends LunaticConfigKey implements MessageKey {

    private final Map<String, String> defaultMessages = new HashMap<>();
    private final List<LunaticPlaceholder> placeholders = new ArrayList<>();
    private boolean withPrefix = true;

    public LunaticMessageKey(String key) {
        super(key);
    }

    @Override
    public MessageKey defaultValue(String defaultMessage) {
        return defaultValue("en", defaultMessage);
    }

    @Override
    public String getDefault() {
        return getDefault("en");
    }

    @Override
    public MessageKey defaultValue(String lang, String defaultMessage) {
        defaultMessages.put(lang.toLowerCase(), defaultMessage);
        return this;
    }

    @Override
    public String getDefault(String lang) {
        return defaultMessages.get(lang.toLowerCase());
    }

    @Override
    public MessageKey noPrefix() {
        this.withPrefix = false;
        return this;
    }

    @Override
    public MessageKey withPrefix() {
        this.withPrefix = true;
        return this;
    }

    @Override
    public boolean isWithPrefix() {
        return withPrefix;
    }

    @Override
    public String asString() {
        return "messages." + getKey();
    }
}
