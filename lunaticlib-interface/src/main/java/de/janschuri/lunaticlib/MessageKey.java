package de.janschuri.lunaticlib;

import java.util.List;

public interface MessageKey extends ConfigKey {
    boolean isWithPrefix();
    MessageKey noPrefix();
    MessageKey withPrefix();
    String getDefault();
    String getDefault(String lang);
    MessageKey defaultValue(String defaultMessage);
    MessageKey defaultValue(String lang, String defaultMessage);
}
