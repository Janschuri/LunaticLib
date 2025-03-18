package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

public interface Placeholder {
    String getKey();

    Component getValue();
}
