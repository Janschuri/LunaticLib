package de.janschuri.lunaticlib;

import java.util.List;

public interface ConfigKey<T> {
    T getDefault();
    String asString();
    String getKey();
    List<String> getKeyInlineComments();
    List<String> getKeyBlockComments();
    List<String> getValueInlineComments();
    List<String> getValueBlockComments();
    ConfigKey defaultValue(T defaultValue);
    ConfigKey keyInlineComment(String comment);
    ConfigKey keyBlockComment(String comment);
    ConfigKey valueInlineComment(String comment);
    ConfigKey valueBlockComment(String comment);
}
