package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.ConfigKey;

import java.util.ArrayList;
import java.util.List;

public class LunaticConfigKey implements ConfigKey {

    private final String key;
    private Object defaultValue;

    private final List<String> keyInlineComments = new ArrayList<>();
    private final List<String> keyBlockComments = new ArrayList<>();
    private final List<String> valueInlineComments = new ArrayList<>();
    private final List<String> valueBlockComments = new ArrayList<>();

    public LunaticConfigKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public String asString() {
        return key;
    }

    public ConfigKey defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ConfigKey keyInlineComment(String comment) {
        keyInlineComments.add(comment);
        return this;
    }

    public ConfigKey keyBlockComment(String comment) {
        keyBlockComments.add(comment);
        return this;
    }

    public ConfigKey valueInlineComment(String comment) {
        valueInlineComments.add(comment);
        return this;
    }

    public ConfigKey valueBlockComment(String comment) {
        valueBlockComments.add(comment);
        return this;
    }

    @Override
    public List<String> getKeyInlineComments() {
        return keyInlineComments;
    }

    @Override
    public List<String> getKeyBlockComments() {
        return keyBlockComments;
    }

    @Override
    public List<String> getValueInlineComments() {
        return valueInlineComments;
    }

    @Override
    public List<String> getValueBlockComments() {
        return valueBlockComments;
    }
}
