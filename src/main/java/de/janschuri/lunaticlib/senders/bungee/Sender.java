package de.janschuri.lunaticlib.senders.bungee;

import de.janschuri.lunaticlib.BungeeLunaticLib;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import de.janschuri.lunaticlib.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class Sender extends AbstractSender {

    CommandSender sender;

    public Sender(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public boolean sendMessage(String message) {
        sender.sendMessage(message);
        return true;
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        TextComponent component = new TextComponent(message.getText());

        if (message.getCommand() != null) {
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.getCommand()));
        }
        if (message.getHoverText() != null) {
            component.setHoverEvent(new TextComponent(message.getHoverText()).getHoverEvent());
        }
        if (message.getColor() != null) {
            component.setColor(ChatColor.of(Utils.hexToColor(message.getColor())));
        }

        sender.sendMessage(component);
        return true;
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        TextComponent msg = new TextComponent(message.getText());

        TextComponent accept = new TextComponent(" ✓");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.getConfirmCommand()));
        accept.setHoverEvent(new TextComponent(message.getConfirmHoverText()).getHoverEvent());

        TextComponent decline = new TextComponent(" ❌");
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.getCancelCommand()));
        decline.setHoverEvent(new TextComponent(message.getCancelHoverText()).getHoverEvent());

        sender.sendMessage(msg, accept, decline);
        return true;
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> messages) {

        TextComponent component = new TextComponent("");

        for (ClickableMessage message : messages) {
            TextComponent msg = new TextComponent(message.getText());

            if (message.getCommand() != null) {
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, message.getCommand()));
            }
            if (message.getHoverText() != null) {
                msg.setHoverEvent(new TextComponent(message.getHoverText()).getHoverEvent());
            }
            if (message.getColor() != null) {
                msg.setColor(ChatColor.of(Utils.hexToColor(message.getColor())));
            }

            component.addExtra(msg);
        }

        sender.sendMessage(component);
        return true;
    }
}
