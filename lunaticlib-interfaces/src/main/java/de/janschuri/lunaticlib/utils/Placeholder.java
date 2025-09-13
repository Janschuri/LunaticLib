package de.janschuri.lunaticlib.utils;

import net.kyori.adventure.text.Component;

public interface Placeholder {
    String getKey();

    Component getValue();
}
