package de.janschuri.lunaticlib.utils.logger;

import com.velocitypowered.api.command.CommandMeta;
import de.janschuri.lunaticlib.LunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLogger extends AbstractLogger {
    private final JavaPlugin plugin;

    public BukkitLogger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void debug(String msg) {
        Bukkit.getLogger().info( "[" +plugin.getName() + "] " + "[DEBUG] " + msg + ChatColor.AQUA);
    }
    @Override
    public void info(String msg) {
        Bukkit.getLogger().info("[" + plugin.getName() + "] " + msg);
    }
    @Override
    public void warn(String msg) {
        Bukkit.getLogger().warning("[" + plugin.getName() + "] " + msg);
    }
    @Override
    public void error(String msg) {
        Bukkit.getLogger().severe("[" + plugin.getName() + "] " + msg);
    }
}
