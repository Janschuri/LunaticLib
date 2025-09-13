package de.janschuri.lunaticlib.sender;

import net.kyori.adventure.text.Component;

public interface Sender {
    boolean hasPermission(String permission);
    boolean sendMessage(String message);
    boolean sendMessage(Component message);
}
