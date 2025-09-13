package de.janschuri.lunaticlib.commands.platform.impl.sender;

import de.janschuri.lunaticlib.commands.platform.WaterfallLunaticLibCommands;
import de.janschuri.lunaticlib.sender.PlayerSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;


public class PlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public PlayerSenderImpl(UUID uuid) {
        super(WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid));
        this.uuid = uuid;
    }

    public PlayerSenderImpl(ProxiedPlayer player) {
        super(player);
        this.uuid = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        ProxiedPlayer player = WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }

        return null;
    }

    @Override
    public boolean chat(String message) {
        WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid).chat(message);
        return true;
    }

    @Override
    public String getServerName() {
        return WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }
    @Override
    public boolean isOnline() {
        return WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid) != null;
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName().equals(getServerName());
    }

    @Override
    public void runCommand(String command) {
        WaterfallLunaticLibCommands.getInstance().getProxy().getPlayer(uuid).chat("/" + command);
    }
}
