package de.janschuri.lunaticlib;

public interface MessageKey {
    String getDefaultMessage();
    String getDefaultMessage(String lang);

    boolean isWithPrefix();

    MessageKey noPrefix();
}
