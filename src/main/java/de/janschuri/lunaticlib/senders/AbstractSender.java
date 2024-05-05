package de.janschuri.lunaticlib.senders;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.CommandSender;

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

    public static AbstractSender getSender(CommandSource source) {
        switch (LunaticLib.getPlatform()) {
            case VELOCITY:
                if (source instanceof Player) {
                    return new de.janschuri.lunaticlib.senders.velocity.PlayerSender(source);
                } else {
                    return new de.janschuri.lunaticlib.senders.velocity.Sender(source);
                }
            default:
                return null;
        }
    }

    public static AbstractSender getSender(CommandSender sender) {
        switch (LunaticLib.getPlatform()) {
            case PAPER:
                if (sender instanceof org.bukkit.entity.Player) {
                    return new de.janschuri.lunaticlib.senders.paper.PlayerSender(sender);
                } else {
                    return new de.janschuri.lunaticlib.senders.paper.Sender(sender);
                }
            default:
                return null;
        }
    }

    public static AbstractSender getSender(net.md_5.bungee.api.CommandSender sender) {
        switch (LunaticLib.getPlatform()) {
            case BUNGEE:
                if (sender instanceof ProxiedPlayer) {
                    return new de.janschuri.lunaticlib.senders.bungee.PlayerSender(sender);
                } else {
                    return new de.janschuri.lunaticlib.senders.bungee.Sender(sender);
                }
            default:
                return null;
        }
    }

    public static AbstractPlayerSender getPlayerSender(UUID uuid) {
        switch (LunaticLib.getPlatform()) {
            case PAPER:
                return new de.janschuri.lunaticlib.senders.paper.PlayerSender(uuid);
            case VELOCITY:
                return new de.janschuri.lunaticlib.senders.velocity.PlayerSender(uuid);
            case BUNGEE:
                return new de.janschuri.lunaticlib.senders.bungee.PlayerSender(uuid);
            default:
                return null;
        }
    }

    public static AbstractPlayerSender getPlayerSender(String name) {
        switch (LunaticLib.getPlatform()) {
            case PAPER:
                return new de.janschuri.lunaticlib.senders.paper.PlayerSender(name);
            case VELOCITY:
                return new de.janschuri.lunaticlib.senders.velocity.PlayerSender(name);
            case BUNGEE:
                return new de.janschuri.lunaticlib.senders.bungee.PlayerSender(name);
            default:
                return null;
        }
    }
}
