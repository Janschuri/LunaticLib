package de.janschuri.lunaticlib.platform.paper.sender;

import de.janschuri.lunaticlib.sender.PlayerSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperPlayerSender extends PaperSender implements PlayerSender<Player, CommandSender> {

    private final Player player;

    public PaperPlayerSender(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public String getServerName() {
        return Bukkit.getServer().getName();
    }

    @Override
    public void chat(String message) {
        player.chat(message);
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public String getName() {
        return player.getName();
    }

    @Override
    public void runCommand(String command) {
        player.performCommand(command);
    }

    @Override
    public Player getHandle() {
        return player;
    }
}
