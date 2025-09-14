package de.janschuri.lunaticlib.commands.platform.velocity.impl;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.platform.Platform;
import de.janschuri.lunaticlib.commands.platform.velocity.VelocityLunaticLibCommands;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.VelocityPlayerSenderImpl;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.SenderImpl;
import de.janschuri.lunaticlib.sender.platform.velocity.impl.VelocitySenderPlatformImpl;

import java.util.ArrayList;
import java.util.Collection;

import static de.janschuri.lunaticlib.commands.platform.velocity.VelocityLunaticLibCommands.getProxy;

public class PlatformImpl extends VelocitySenderPlatformImpl implements Platform<PluginContainer, CommandSource> {

    @Override
    public Sender getSender(CommandSource sender) {
        if (sender instanceof Player) {
            return new VelocityPlayerSenderImpl((Player) sender);
        }

        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(PluginContainer pluginContainer, Command lunaticCommand) {
        CommandManager commandManager = getProxy().getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder(lunaticCommand.getName())
                .aliases(lunaticCommand.getAliases().toArray(new String[0]))
                .plugin(pluginContainer)
                .build();


        commandManager.register(commandMeta, new VelocityLunaticLibCommands.VelocityCommand(lunaticCommand));
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<Player> proxyPlayers = getProxy().getAllPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (Player player : proxyPlayers) {
            players.add(new VelocityPlayerSenderImpl(player));
        }

        return players;
    }
}
