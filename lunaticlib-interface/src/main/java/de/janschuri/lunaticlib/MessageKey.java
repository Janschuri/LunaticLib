package de.janschuri.lunaticlib;

import java.util.List;

public interface MessageKey extends ConfigKey {
    boolean isWithPrefix();
    MessageKey noPrefix();
    MessageKey withPrefix();
    String getDefaultMessage();
    String getDefaultMessage(String lang);
}
