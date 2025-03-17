package de.janschuri.lunaticlib.platform.bungee.sender;

import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.bungee.external.AdventureAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.CommandSender;

public class SenderImpl implements Sender {

    CommandSender sender;

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
        return AdventureAPI.sendMessage(sender, msg);
    }

    @Override
    public boolean sendMessage(Component message) {
        if (sender == null) {
            return false;
        }
        return AdventureAPI.sendMessage(sender, message);
    }
}
