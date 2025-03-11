package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.MessageKey;

public class LunaticMessageKey extends LunaticLanguageKey implements MessageKey {

    private boolean withPrefix = true;

    public LunaticMessageKey(String key) {
        super(key);
    }

    @Override
    public LunaticMessageKey defaultMessage(String lang, String defaultMessage) {
        defaultValue(lang, defaultMessage);
        return this;
    }

    @Override
    public String getDefaultMessage(String lang) {
        return getDefault(lang);
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
