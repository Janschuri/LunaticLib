package de.janschuri.lunaticlib.platform.velocity.commands;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.CommandAdapter;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocityPlayerSender;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderAdapter;
import de.janschuri.lunaticlib.sender.PlayerSender;

import java.util.ArrayList;
import java.util.Collection;

public class VelocityCommandAdapter extends VelocitySenderAdapter implements CommandAdapter<PluginContainer, CommandSource> {

    private final Object instance;
    private final ProxyServer proxy;

    public VelocityCommandAdapter(Object instance, ProxyServer proxyServer) {
        super(proxyServer);
        this.instance = instance;
        this.proxy = proxyServer;
    }

    @Override
    public void registerCommand(PluginContainer pluginContainer, Command lunaticCommand) {
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder(lunaticCommand.getName())
                .aliases(lunaticCommand.getAliases().toArray(new String[0]))
                .plugin(pluginContainer)
                .build();


        commandManager.register(commandMeta, new VelocityCommand(proxy, instance, lunaticCommand));
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<Player> proxyPlayers = proxy.getAllPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (Player player : proxyPlayers) {
            players.add(new VelocityPlayerSender(player));
        }

        return players;
    }
}
