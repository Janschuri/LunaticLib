package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.Placeholder;
import net.kyori.adventure.text.Component;

public class LunaticPlaceholder implements Placeholder {

    private final String key;
    private final Component value;

    public LunaticPlaceholder(String key, Component value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Component getValue() {
        return value;
    }
}
