package de.janschuri.lunaticlib.platform.bukkit.sender;

import de.janschuri.lunaticlib.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public class SenderImpl implements Sender {

    private final CommandSender sender;

    public SenderImpl(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (sender == null) {
            return false;
        }

        return sender.hasPermission(permission);
    }

    @Override
    public boolean sendMessage(String message) {
        if (sender == null) {
            return false;
        }

        TextComponent msg = LegacyComponentSerializer.legacy('§').deserialize(message);
        sender.sendMessage(msg);
        return true;
    }

    @Override
    public boolean sendMessage(Component message) {
        if (sender == null) {
            return false;
        }

        sender.sendMessage(message);
        return true;
    }
}
