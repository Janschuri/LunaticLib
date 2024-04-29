package de.janschuri.lunaticlib.utils.logger;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BungeeLogger extends AbstractLogger {
    private final Plugin plugin;

    public BungeeLogger(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void debug(String msg) {
        plugin.getLogger().info(  ANSI_BLUE + "[" + plugin + "] " + ANSI_AQUA + "[DEBUG] " + msg + ANSI_RESET);
    }
    @Override
    public void info(String msg) {
        plugin.getLogger().info(ANSI_BLUE + "[" + plugin + "] " + ANSI_RESET + msg);
    }
    @Override
    public void warn(String msg) {
        plugin.getLogger().warning("[" + plugin + "] " + msg);
    }
    @Override
    public void error(String msg) {
        plugin.getLogger().severe("[" + plugin + "] " + msg);
    }
}
