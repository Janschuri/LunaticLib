package de.janschuri.lunaticlib.commands.platform.impl;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.platform.PaperLunaticLibCommands;
import de.janschuri.lunaticlib.commands.platform.Platform;
import de.janschuri.lunaticlib.commands.platform.impl.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.commands.platform.impl.sender.SenderImpl;
import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
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

public class PlatformImpl implements Platform<JavaPlugin, CommandSender> {

    @Override
    public Sender getSender(CommandSender sender) {
        if (sender instanceof Player player) {
            PlayerSender playerSender = new PlayerSenderImpl(player);
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

            synchronized (aliases) {
                aliases.forEach(alias -> {
                    commandMap.register(alias, plugin.getName(), cmd);
                });
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        plugin.getCommand(command.getName()).setPermission(command.getPermission());

        plugin.getCommand(command.getName()).setExecutor(new PaperLunaticLibCommands.PaperCommand(command));
        plugin.getCommand(command.getName()).setTabCompleter(new PaperLunaticLibCommands.PaperCommand(command));
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
