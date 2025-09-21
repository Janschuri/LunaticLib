package de.janschuri.lunaticlib.platform.waterfall.commands;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.CommandAdapter;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallSenderAdapter;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallPlayerSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;

public class WaterfallCommandAdapter extends WaterfallSenderAdapter implements CommandAdapter<Plugin, CommandSender> {

    private final Plugin plugin;

    public WaterfallCommandAdapter(Plugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        WaterfallCommand bungeeCommand = new WaterfallCommand(plugin, command);
        plugin.getProxy().getPluginManager().registerCommand(plugin, bungeeCommand);
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<ProxiedPlayer> proxiedPlayers = plugin.getProxy().getPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (ProxiedPlayer player : proxiedPlayers) {
            players.add(new WaterfallPlayerSender(player));
        }

        return players;
    }
}
