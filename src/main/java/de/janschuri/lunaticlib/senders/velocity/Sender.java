package de.janschuri.lunaticlib.senders.velocity;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.config.Language;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.UUID;

public class Sender extends AbstractSender {

    CommandSource sender;

    public Sender(CommandSource source) {
        this.sender = source;
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }

    @Override
    public boolean sendMessage(String message) {
        TextComponent msg = LegacyComponentSerializer.legacy('§').deserialize(message);
        sender.sendMessage(msg);
        return true;
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        sender.sendMessage(
                LegacyComponentSerializer.legacy('§').deserialize(message.getText())
                        .append(Component.text(" ✓", NamedTextColor.GREEN, TextDecoration.BOLD).clickEvent(ClickEvent.runCommand(
                                message.getConfirmCommand()
                        )))
                        .hoverEvent(HoverEvent.showText(
                                LegacyComponentSerializer.legacy('§').deserialize(message.getConfirmHoverText())
                        ))
                        .append(Component.text(" ❌", NamedTextColor.RED, TextDecoration.BOLD).clickEvent(ClickEvent.runCommand(
                                message.getCancelCommand()
                        )))
                        .hoverEvent(HoverEvent.showText(
                                LegacyComponentSerializer.legacy('§').deserialize(message.getCancelHoverText())
                        ))
                        .toBuilder().build());
        return true;
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        sender.sendMessage(
                LegacyComponentSerializer.legacy('§').deserialize(message.getText())
                        .clickEvent(ClickEvent.runCommand(message.getCommand()))
                        .hoverEvent(HoverEvent.showText(
                                LegacyComponentSerializer.legacy('§').deserialize(message.getHoverText())
                        ))
                        .toBuilder().build());
        return true;
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> msg) {
        Component component = Component.empty();
        for (ClickableMessage message : msg) {
            Component text = LegacyComponentSerializer.legacy('§').deserialize(message.getText());
            if (message.getCommand() != null) {
                text = text.clickEvent(ClickEvent.runCommand(message.getCommand()));
            }
            if (message.getHoverText() != null) {
                text = text.hoverEvent(HoverEvent.showText(
                        LegacyComponentSerializer.legacy('§').deserialize(message.getHoverText())
                ));
            }
            if (message.getColor() != null) {
                text = text.color(TextColor.fromHexString(message.getColor()));
            }
            component = component.append(text);
        }
        sender.sendMessage(component);
        return true;
    }

    @Override
    public AbstractPlayerSender getPlayerCommandSender(UUID uuid) {
        return new PlayerSender(uuid);
    }

    @Override
    public AbstractPlayerSender getPlayerCommandSender(String name) {
        return new PlayerSender(name);
    }
}
