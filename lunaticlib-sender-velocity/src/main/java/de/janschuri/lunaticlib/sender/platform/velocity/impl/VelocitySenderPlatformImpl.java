package de.janschuri.lunaticlib.sender.platform.velocity.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.sender.platform.SenderPlatform;

import java.util.UUID;

public class VelocitySenderPlatformImpl implements SenderPlatform<PluginContainer, CommandSource> {

    @Override
    public Sender getSender(CommandSource sender) {
        if (sender instanceof Player) {
            return new VelocityPlayerSenderImpl((Player) sender);
        }

        return new SenderImpl(sender);
    }

    @Override
    public PlayerSender getPlayerSender(UUID uuid) {
        return new VelocityPlayerSenderImpl(uuid);
    }
}
