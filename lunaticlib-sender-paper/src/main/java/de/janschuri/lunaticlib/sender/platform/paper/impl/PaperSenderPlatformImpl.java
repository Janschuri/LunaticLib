package de.janschuri.lunaticlib.sender.platform.paper.impl;

import de.janschuri.lunaticlib.sender.platform.SenderPlatform;
import de.janschuri.lunaticlib.sender.platform.paper.impl.sender.PaperPlayerSenderImpl;
import de.janschuri.lunaticlib.sender.platform.paper.impl.sender.SenderImpl;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PaperSenderPlatformImpl implements SenderPlatform<JavaPlugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof Player player) {
            PlayerSender playerSender = new PaperPlayerSenderImpl(player);
            return playerSender;
        }

        return new SenderImpl(sender);
    }

    @Override
    public PlayerSender getPlayerSender(UUID uuid) {
        return new PaperPlayerSenderImpl(uuid);
    }
}
