package de.janschuri.lunaticlib;

public class MessageKey {

    private String key;
    private String defaultMessage;

    public MessageKey(String key) {
        this.key = key;
    }

    public MessageKey defaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    @Override
    public String toString() {
        return "messages." + key;
    }
}
