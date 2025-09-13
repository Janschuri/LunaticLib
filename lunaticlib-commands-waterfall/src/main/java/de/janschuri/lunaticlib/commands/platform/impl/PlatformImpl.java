package de.janschuri.lunaticlib.commands.platform.impl;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.platform.Platform;
import de.janschuri.lunaticlib.commands.platform.WaterfallLunaticLibCommands;
import de.janschuri.lunaticlib.commands.platform.impl.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.commands.platform.impl.sender.SenderImpl;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;

public class PlatformImpl implements Platform<Plugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new PlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        WaterfallLunaticLibCommands.WaterfallCommand bungeeCommand = new WaterfallLunaticLibCommands.WaterfallCommand(command);

        plugin.getProxy().getPluginManager().registerCommand(WaterfallLunaticLibCommands.getInstance(), bungeeCommand);
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<ProxiedPlayer> proyiedPlayers = WaterfallLunaticLibCommands.getInstance().getProxy().getPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (ProxiedPlayer player : proyiedPlayers) {
            players.add(new PlayerSenderImpl(player));
        }

        return players;
    }
}
