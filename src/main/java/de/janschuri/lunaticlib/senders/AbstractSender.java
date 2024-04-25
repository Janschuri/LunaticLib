package de.janschuri.lunaticlib.senders;

import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;

import java.util.List;
import java.util.UUID;

public abstract class AbstractSender {
    public AbstractSender() {
    }
    public abstract boolean hasPermission(String permission);
    public abstract boolean sendMessage(String message);
    public abstract boolean sendMessage(ClickableMessage message);
    public abstract boolean sendMessage(ClickableDecisionMessage message);
    public abstract boolean sendMessage(List<ClickableMessage> msg);
    public abstract AbstractPlayerSender getPlayerCommandSender(UUID uuid);
    public abstract AbstractPlayerSender getPlayerCommandSender(String name);
}
