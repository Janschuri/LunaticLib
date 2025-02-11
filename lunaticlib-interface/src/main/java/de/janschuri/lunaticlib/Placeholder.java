package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

public class Placeholder {

    private final String key;
    private final Component value;

    public Placeholder(String key, Component value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Component getValue() {
        return value;
    }
}
