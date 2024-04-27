package de.janschuri.lunaticlib.senders.velocity;

import com.velocitypowered.api.command.CommandSource;
import de.janschuri.lunaticlib.VelocityLunaticLib;
import de.janschuri.lunaticlib.senders.AbstractPlayerSender;
import de.janschuri.lunaticlib.utils.ClickableDecisionMessage;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import de.janschuri.lunaticlib.utils.FutureRequests;
import de.janschuri.lunaticlib.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;

public class PlayerSender extends AbstractPlayerSender {

    private final UUID uuid;
    public PlayerSender(CommandSource sender) {
        super(((com.velocitypowered.api.proxy.Player) sender).getUniqueId());
        this.uuid = ((com.velocitypowered.api.proxy.Player) sender).getUniqueId();
    }

    public PlayerSender(UUID uuid) {
        super(uuid);
        this.uuid = uuid;
    }

    public PlayerSender(String name) {
        super(FutureRequests.getUniqueId(name));
        this.uuid = FutureRequests.getUniqueId(name);
    }


    @Override
    public boolean sendMessage(String message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        player.ifPresent(value -> value.sendMessage(
                Component.text(message)
        ));
        return player.isPresent();
    }

    @Override
    public boolean sendMessage(ClickableMessage message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        player.ifPresent(value -> value.sendMessage(
                        LegacyComponentSerializer.legacy('§').deserialize(message.getText())
                                .clickEvent(ClickEvent.runCommand(message.getCommand()))
                                .hoverEvent(HoverEvent.showText(
                                        LegacyComponentSerializer.legacy('§').deserialize(message.getHoverText())
                                ))
                                .toBuilder().build()
        ));
        return player.isPresent();
    }

    @Override
    public boolean sendMessage(ClickableDecisionMessage message) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        player.ifPresent(value -> value.sendMessage(
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
                        .toBuilder().build()
        ));
        return player.isPresent();
    }

    @Override
    public boolean sendMessage(List<ClickableMessage> msg) {
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            Component component = Component.empty();
            for (ClickableMessage message : msg) {
                Component text = Component.text(message.getText());
                if (message.getCommand() != null) {
                    text = text.clickEvent(ClickEvent.runCommand(message.getCommand()));
                }
                if (message.getHoverText() != null) {
                    text = text.hoverEvent(HoverEvent.showText(Component.text(message.getHoverText())));
                }
                if (message.getColor() != null) {
                    text = text.color(TextColor.fromHexString(message.getColor()));
                }
                component = component.append(text);
            }
            player.get().sendMessage(component);
        }
        return player.isPresent();
    }

    @Override
    public UUID getUniqueId(String name) {
        return FutureRequests.getUniqueId(name);
    }

    @Override
    public String getName() {
        return FutureRequests.getName(uuid);
    }

    @Override
    public boolean chat(String message) {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        if (playerOptional.isPresent()) {
            playerOptional.get().sendMessage(
                LegacyComponentSerializer.legacy('§').deserialize(message)
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean hasItemInMainHand() {
        return FutureRequests.hasItemInMainHand(uuid);
    }

    @Override
    public byte[] getItemInMainHand() {
        return FutureRequests.getItemInMainHand(uuid);
    }

    @Override
    public boolean removeItemInMainHand() {
        return FutureRequests.removeItemInMainHand(uuid);
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        return FutureRequests.giveItemDrop(uuid, item);
    }

    @Override
    public boolean hasPermission(String permission) {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.hasPermission(permission)).orElse(false);
    }

    @Override
    public String getServerName() {
        Optional<com.velocitypowered.api.proxy.Player> playerOptional = VelocityLunaticLib.getProxy().getPlayer(uuid);
        return playerOptional.map(player -> player.getCurrentServer().get().getServerInfo().getName()).orElse(null);
    }

    @Override
    public double[] getPosition() {
        return FutureRequests.getPosition(uuid);
    }

    @Override
    public boolean isOnline() {
        Collection<com.velocitypowered.api.proxy.Player> players = VelocityLunaticLib.getProxy().getAllPlayers();
        for (com.velocitypowered.api.proxy.Player player : players) {
            if (player.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInRange(UUID playerUUID, double range) {
        return FutureRequests.isInRange(uuid, playerUUID, range);
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public AbstractPlayerSender getPlayerCommandSender(UUID uuid) {
        return new PlayerSender(uuid);
    }

    @Override
    public AbstractPlayerSender getPlayerCommandSender(String name) {
        return new PlayerSender(name);
    }

    @Override
    public boolean isSameServer(UUID player1UUID) {
        Optional<com.velocitypowered.api.proxy.Player> player1 = VelocityLunaticLib.getProxy().getPlayer(player1UUID);
        Optional<com.velocitypowered.api.proxy.Player> player = VelocityLunaticLib.getProxy().getPlayer(uuid);

        return player1.isPresent() && player.isPresent() && player1.get().getCurrentServer().get().getServerInfo().getName().equals(player.get().getCurrentServer().get().getServerInfo().getName());

    }
}
