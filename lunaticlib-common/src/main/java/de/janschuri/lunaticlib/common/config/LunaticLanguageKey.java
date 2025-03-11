package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.LanguageKey;

import java.util.HashMap;
import java.util.Map;

public class LunaticLanguageKey extends LunaticConfigKey implements LanguageKey {


    public LunaticLanguageKey(String key) {
        super(key);
    }

    @Override
    public Map<String, String> getDefault() {
        return (Map<String, String>) super.getDefault();
    }

    @Override
    public String getDefault(String lang) {
        if (!getDefault().containsKey(lang.toLowerCase())) {
            return getDefault().get("en");
        }

        return getDefault().get(lang.toLowerCase());
    }

    @Override
    public LunaticLanguageKey defaultValues(Map<String, String> defaultMessages) {
        super.defaultValue(defaultMessages);
        return this;
    }

    @Override
    public LunaticLanguageKey defaultValue(String lang, String defaultMessage) {
        Map<String, String> defaultMessages = getDefault();
        defaultMessages.put(lang.toLowerCase(), defaultMessage);
        super.defaultValue(defaultMessages);
        return this;
    }

    @Override
    public LunaticLanguageKey keyInlineComment(String comment) {
        super.keyInlineComment(comment);
        return this;
    }

    @Override
    public LunaticLanguageKey keyBlockComment(String comment) {
        super.keyBlockComment(comment);
        return this;
    }

    @Override
    public LunaticLanguageKey valueInlineComment(String comment) {
        super.valueInlineComment(comment);
        return this;
    }

    @Override
    public LunaticLanguageKey valueBlockComment(String comment) {
        super.valueBlockComment(comment);
        return this;
    }
}
