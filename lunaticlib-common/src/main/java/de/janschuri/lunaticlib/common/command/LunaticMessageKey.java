package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.ConfigKey;
import de.janschuri.lunaticlib.MessageKey;
import de.janschuri.lunaticlib.common.config.LunaticConfig;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunaticMessageKey extends LunaticConfigKey implements MessageKey {

    private final Map<String, String> defaultMessages = new HashMap<>();
    private boolean withPrefix = true;

    public LunaticMessageKey(String key) {
        super(key);
    }

    public LunaticMessageKey defaultMessage(String defaultMessage) {
        return defaultMessage("en", defaultMessage);
    }

    public LunaticMessageKey defaultMessage(String lang, String defaultMessage) {
        Logger.debugLog("Test:" + defaultMessages);
        defaultMessages.put(lang.toLowerCase(), defaultMessage);
        return this;
    }

    @Override
    public Object getDefault() {
        return defaultMessages;
    }

    @Override
    public String getDefaultMessage() {
        return getDefaultMessage("en");
    }

    @Override
    public String getDefaultMessage(String lang) {
        return defaultMessages.get(lang.toLowerCase());
    }

    @Override
    public LunaticMessageKey noPrefix() {
        this.withPrefix = false;
        return this;
    }

    @Override
    public LunaticMessageKey withPrefix() {
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

    @Override
    public LunaticMessageKey keyInlineComment(String comment) {
        super.keyInlineComment(comment);
        return this;
    }

    @Override
    public LunaticMessageKey keyBlockComment(String comment) {
        super.keyBlockComment(comment);
        return this;
    }

    @Override
    public LunaticMessageKey valueInlineComment(String comment) {
        super.valueInlineComment(comment);
        return this;
    }

    @Override
    public LunaticMessageKey valueBlockComment(String comment) {
        super.valueBlockComment(comment);
        return this;
    }
}
