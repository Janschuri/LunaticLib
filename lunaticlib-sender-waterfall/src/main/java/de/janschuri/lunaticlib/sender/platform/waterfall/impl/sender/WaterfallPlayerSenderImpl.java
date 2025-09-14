package de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender;

import de.janschuri.lunaticlib.sender.platform.waterfall.WaterfallLunaticLibCommands;
import de.janschuri.lunaticlib.sender.PlayerSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;


public class WaterfallPlayerSenderImpl extends SenderImpl implements PlayerSender {

    private final UUID uuid;

    public WaterfallPlayerSenderImpl(UUID uuid) {
        super(WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid));
        this.uuid = uuid;
    }

    public WaterfallPlayerSenderImpl(ProxiedPlayer player) {
        super(player);
        this.uuid = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        ProxiedPlayer player = WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }

        return null;
    }

    @Override
    public boolean chat(String message) {
        WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid).chat(message);
        return true;
    }

    @Override
    public String getServerName() {
        return WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName();
    }
    @Override
    public boolean isOnline() {
        return WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid) != null;
    }

    @Override
    public boolean exists() {
        return uuid != null;
    }

    @Override
    public boolean isSameServer(UUID uuid) {
        return WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid).getServer().getInfo().getName().equals(getServerName());
    }

    @Override
    public void runCommand(String command) {
        WaterfallLunaticLibCommands.getPluginInstance().getProxy().getPlayer(uuid).chat("/" + command);
    }
}
