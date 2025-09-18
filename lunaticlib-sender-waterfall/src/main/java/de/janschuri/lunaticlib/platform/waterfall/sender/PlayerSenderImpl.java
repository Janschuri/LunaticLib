package de.janschuri.lunaticlib.platform.waterfall.sender;

import de.janschuri.lunaticlib.sender.PlayerSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;


public class PlayerSenderImpl extends SenderImpl implements PlayerSender<ProxiedPlayer, CommandSender> {

    private final ProxiedPlayer player;

    public PlayerSenderImpl(ProxiedPlayer player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void chat(String message) {
        player.chat(message);
    }

    @Override
    public String getServerName() {
        return player.getServer().getInfo().getName();
    }

    @Override
    public void runCommand(String command) {
        player.chat("/" + command);
    }

    @Override
    public ProxiedPlayer getHandle() {
        return player;
    }
}
