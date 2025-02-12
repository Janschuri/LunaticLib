package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.*;
import de.janschuri.lunaticlib.platform.bukkit.commands.BukkitCommand;
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
        if (sender instanceof Player player) {
            PlayerSenderImpl playerSender = new PlayerSenderImpl(player);
            return playerSender;
        }

        return new SenderImpl(sender);
    }

    @Override
    public void registerCommand(JavaPlugin plugin, Command command) {
        PluginCommand cmd = plugin.getCommand(command.getName());
        assert cmd != null;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            List<String> aliases = command.getAliases();

            Logger.debugLog("Registering command " + command.getName() + " with aliases " + aliases.toString());
            Logger.debugLog("Command: " + cmd);

            synchronized (aliases) {
                aliases.forEach(alias -> {
                    commandMap.register(alias, plugin.getName(), cmd);
                });
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        plugin.getCommand(command.getName()).setPermission(command.getPermission());

        plugin.getCommand(command.getName()).setExecutor(new BukkitCommand(command));
        plugin.getCommand(command.getName()).setTabCompleter(new BukkitCommand(command));
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
