package de.janschuri.lunaticlib.sender.platform.waterfall.impl;

import de.janschuri.lunaticlib.sender.platform.SenderPlatform;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender.WaterfallPlayerSenderImpl;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender.SenderImpl;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class PlatformImpl implements SenderPlatform<Plugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new WaterfallPlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }

    @Override
    public PlayerSender getPlayerSender(UUID uuid) {
        return new WaterfallPlayerSenderImpl(uuid);
    }
}
