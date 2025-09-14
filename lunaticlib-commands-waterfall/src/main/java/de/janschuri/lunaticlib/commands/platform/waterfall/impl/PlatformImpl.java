package de.janschuri.lunaticlib.commands.platform.waterfall.impl;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.platform.Platform;
import de.janschuri.lunaticlib.commands.platform.waterfall.WaterfallLunaticLibCommands;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender.WaterfallPlayerSenderImpl;
import de.janschuri.lunaticlib.sender.platform.waterfall.impl.sender.SenderImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;

public class PlatformImpl extends de.janschuri.lunaticlib.sender.platform.waterfall.impl.PlatformImpl implements Platform<Plugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new WaterfallPlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        WaterfallLunaticLibCommands.WaterfallCommand bungeeCommand = new WaterfallLunaticLibCommands.WaterfallCommand(command);

        plugin.getProxy().getPluginManager().registerCommand(plugin, bungeeCommand);
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<ProxiedPlayer> proxiedPlayers = WaterfallLunaticLibCommands.getInstance().getProxy().getPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (ProxiedPlayer player : proxiedPlayers) {
            players.add(new WaterfallPlayerSenderImpl(player));
        }

        return players;
    }
}
