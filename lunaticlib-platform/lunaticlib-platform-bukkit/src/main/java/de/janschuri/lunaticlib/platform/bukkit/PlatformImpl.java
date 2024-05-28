package de.janschuri.lunaticlib.platform.bukkit;

import de.janschuri.lunaticlib.Subcommand;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.platform.*;
import de.janschuri.lunaticlib.platform.bukkit.commands.Command;
import de.janschuri.lunaticlib.platform.bukkit.external.VaultImpl;
import de.janschuri.lunaticlib.platform.bukkit.sender.PlayerSenderImpl;
import de.janschuri.lunaticlib.platform.bukkit.sender.SenderImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.UUID;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

public class PlatformImpl implements Platform<JavaPlugin, CommandSender> {
    @Override
    public boolean sendPluginMessage(String server, byte[] message) {
        BukkitLunaticLib.getInstance().getServer().sendPluginMessage(BukkitLunaticLib.getInstance(), IDENTIFIER, message);
        return false;
    }

    @Override
    public boolean sendPluginMessage(byte[] message) {
        BukkitLunaticLib.getInstance().getServer().sendPluginMessage(BukkitLunaticLib.getInstance(), IDENTIFIER, message);
        return false;
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
    public void registerCommand(JavaPlugin plugin, Subcommand subcommand) {
        PluginCommand cmd = plugin.getCommand(subcommand.getName());
        assert cmd != null;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            subcommand.getAliases().forEach(alias -> {
                commandMap.register(alias, plugin.getName(), cmd);
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        plugin.getCommand(subcommand.getName()).setPermission(subcommand.getPermission());

        plugin.getCommand(subcommand.getName()).setExecutor(new Command(subcommand));
        plugin.getCommand(subcommand.getName()).setTabCompleter(new Command(subcommand));
    }

}
