package de.janschuri.lunaticlib.platform.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.*;
import de.janschuri.lunaticlib.platform.velocity.command.Command;
import de.janschuri.lunaticlib.platform.velocity.external.VaultImpl;
import de.janschuri.lunaticlib.platform.velocity.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.platform.velocity.sender.SenderImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static de.janschuri.lunaticlib.platform.velocity.VelocityLunaticLib.MINECRAFT_CHANNEL_IDENTIFIER;

public class PlatformImpl implements Platform<PluginContainer, CommandSource> {
    public boolean sendPluginMessage(String server, byte[] message) {
        return VelocityLunaticLib.sendPluginMessage(server, message);
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        return VelocityLunaticLib.sendPluginMessage(message);
    }

    public void sendConsoleCommand(String command) {
        VelocityLunaticLib.getProxy().getCommandManager().executeAsync(VelocityLunaticLib.getProxy().getConsoleCommandSource(), command);
    }

    public de.janschuri.lunaticlib.PlayerSender getPlayerSender(UUID uuid) {
        return new PlayerSenderImpl(uuid);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.VELOCITY;
    }

    @Override
    public Vault getVault() {
        return new VaultImpl();
    }


    @Override
    public de.janschuri.lunaticlib.Sender getSender(CommandSource sender) {
        if (sender instanceof Player) {
            return new PlayerSenderImpl((Player) sender);
        }

        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(PluginContainer pluginContainer, LunaticCommand lunaticCommand) {
        CommandManager commandManager = VelocityLunaticLib.getProxy().getCommandManager();

        CommandMeta commandMeta = commandManager.metaBuilder(lunaticCommand.getName())
                .aliases(lunaticCommand.getAliases().toArray(new String[0]))
                .plugin(pluginContainer)
                .build();


        commandManager.register(commandMeta, new Command(lunaticCommand));
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<Player> proxyPlayers = VelocityLunaticLib.getProxy().getAllPlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (Player player : proxyPlayers) {
            players.add(new PlayerSenderImpl(player));
        }

        return players;
    }
}
