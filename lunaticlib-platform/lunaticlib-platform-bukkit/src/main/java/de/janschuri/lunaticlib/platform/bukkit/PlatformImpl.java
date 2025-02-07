package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.*;
import de.janschuri.lunaticlib.platform.bukkit.commands.Command;
import de.janschuri.lunaticlib.platform.bukkit.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.platform.bukkit.sender.SenderImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class PlatformImpl implements Platform<JavaPlugin, CommandSender> {
    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        return BukkitLunaticLib.sendPluginMessage(message);
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        return BukkitLunaticLib.sendPluginMessage(message);
    }

    @Override
    public void sendConsoleCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }

    @Override
    public de.janschuri.lunaticlib.PlayerSender getPlayerSender(UUID uuid) {
        return new PlayerSenderImpl(uuid);
    }

    @Override
    public PlatformType getPlatformType() {
        return PlatformType.BUKKIT;
    }

    @Override
    public Vault getVault() {
        return BukkitLunaticLib.getVault();
    }

    @Override
    public de.janschuri.lunaticlib.Sender getSender(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerSenderImpl playerSender = new PlayerSenderImpl(player);
            return playerSender;
        }

        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(JavaPlugin plugin, LunaticCommand lunaticCommand) {
        PluginCommand cmd = plugin.getCommand(lunaticCommand.getName());
        assert cmd != null;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            List<String> aliases = lunaticCommand.getAliases();

            Logger.debugLog("Registering command " + lunaticCommand.getName() + " with aliases " + aliases.toString());
            Logger.debugLog("Command: " + cmd.toString());

            synchronized (aliases) {
                aliases.forEach(alias -> {
                    commandMap.register(alias, plugin.getName(), cmd);
                });
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        plugin.getCommand(lunaticCommand.getName()).setPermission(lunaticCommand.getPermission());

        plugin.getCommand(lunaticCommand.getName()).setExecutor(new Command(lunaticCommand));
        plugin.getCommand(lunaticCommand.getName()).setTabCompleter(new Command(lunaticCommand));
    }

    @Override
    public JavaPlugin getPlugin() {
        return BukkitLunaticLib.getInstance();
    }

    @Override
    public Collection<PlayerSender> getOnlinePlayers() {
        Collection<? extends Player> bukkitPlayers = Bukkit.getOnlinePlayers();

        Collection<PlayerSender> players = new ArrayList<>();

        for (Player player : bukkitPlayers) {
            players.add(new PlayerSenderImpl(player));
        }

        return players;
    }
}
