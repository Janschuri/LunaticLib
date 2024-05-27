package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;


public class SenderImpl implements Sender {

    CommandSource sender;

    public SenderImpl(CommandSource source) {
        this.sender = source;
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

    public boolean sendMessage(Component message) {
        if (sender == null) {
            return false;
        }

        sender.sendMessage(message);
        return true;
    }
}
