package de.janschuri.lunaticlib.platform.bungee;

import de.janschuri.lunaticlib.Subcommand;
import de.janschuri.lunaticlib.platform.*;
import de.janschuri.lunaticlib.platform.bungee.commands.Command;
import de.janschuri.lunaticlib.platform.bungee.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.platform.bungee.sender.SenderImpl;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class PlatformImpl implements Platform<Plugin, CommandSender> {
    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        if (BungeeLunaticLib.getInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        BungeeLunaticLib.getInstance().getProxy().getServerInfo(server).sendData(IDENTIFIER, message);
        return false;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        if (BungeeLunaticLib.getInstance().getProxy().getOnlineCount() == 0) {
            return false;
        }

        for (ServerInfo server : BungeeLunaticLib.getInstance().getProxy().getServers().values()) {
            server.sendData(LunaticLib.IDENTIFIER, message);
        }

        return true;
    }

    @Override
    public void sendConsoleCommand(String command) {
        BungeeLunaticLib.getInstance().getProxy().getPluginManager().dispatchCommand(BungeeLunaticLib.getInstance().getProxy().getConsole(), command);
    }

    @Override
    public de.janschuri.lunaticlib.PlayerSender getPlayerSender(UUID uuid) {
        return new PlayerSenderImpl(uuid);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.BUNGEE;
    }

    @Override
    public Vault getVault() {
        return null;
    }

    @Override
    public de.janschuri.lunaticlib.Sender getSender(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new PlayerSenderImpl((ProxiedPlayer) sender);
        }
        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(Plugin plugin, Subcommand subcommand) {
        plugin.getProxy().getPluginManager().registerCommand(BungeeLunaticLib.getInstance(), new Command(subcommand));
    }
}
