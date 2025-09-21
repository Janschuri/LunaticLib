package de.janschuri.lunaticlib.platform.paper.sender;

import de.janschuri.lunaticlib.sender.SenderAdapter;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PaperSenderAdapter implements SenderAdapter<CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof Player player) {
            PlayerSender playerSender = new PaperPlayerSender(player);
            return playerSender;
        }

        return new PaperSender(sender);
    }

    @Override
    public PlayerSender getPlayerSender(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return new PaperPlayerSender(player);
        }
        return null;
    }
}
