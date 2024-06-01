package de.janschuri.lunaticlib;

public class MessageKey {

    private String key;

    public MessageKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "messages." + key;
    }
}
