package de.janschuri.lunaticlib;

public interface MessageKey {
    String getDefaultMessage();

    boolean isWithPrefix();

    MessageKey noPrefix();
}
