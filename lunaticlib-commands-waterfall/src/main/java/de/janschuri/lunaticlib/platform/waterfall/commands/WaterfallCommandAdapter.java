package de.janschuri.lunaticlib.platform.waterfall.commands;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.CommandAdapter;
import de.janschuri.lunaticlib.platform.waterfall.sender.WaterfallAdapter;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.platform.waterfall.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.platform.waterfall.sender.SenderImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;

public class WaterfallCommandAdapter extends WaterfallAdapter implements CommandAdapter<Plugin> {

    private final ProxyServer proxy;

    public WaterfallCommandAdapter(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new PlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        WaterfallCommandHandler.WaterfallCommand bungeeCommand = new WaterfallCommandHandler.WaterfallCommand(plugin, command);
        plugin.getProxy().getPluginManager().registerCommand(plugin, bungeeCommand);
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<ProxiedPlayer> proxiedPlayers = proxy.getPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (ProxiedPlayer player : proxiedPlayers) {
            players.add(new PlayerSenderImpl(player));
        }

        return players;
    }
}
