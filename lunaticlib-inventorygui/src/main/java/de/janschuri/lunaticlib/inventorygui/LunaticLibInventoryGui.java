package de.janschuri.lunaticlib.inventorygui;

import de.janschuri.lunaticlib.inventorygui.handler.GUIManager;
import de.janschuri.lunaticlib.inventorygui.listener.GUIListener;
import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.impl.LunaticLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LunaticLibInventoryGui {

    private static JavaPlugin plugin;
    static Logger logger = LunaticLogger.getLogger("LunaticLib-InventoryGui");

    public static void enable(JavaPlugin plugin) {
        LunaticLibInventoryGui.plugin = plugin;

        Bukkit.getServer().getPluginManager().registerEvents(new GUIListener(), getPluginInstance());
    }

    public static void disable() {
        GUIManager.closeAll();
        logger.info("LunaticLib-InventoryGui disabled.");
    }

    public static void logger(Logger logger) {
        LunaticLibInventoryGui.logger = logger;
    }

    public static JavaPlugin getPluginInstance() {
        if (plugin == null) {
            throw new IllegalStateException("LunaticLibInventoryGui is not enabled. Please call LunaticLibInventoryGui.enable(plugin) first.");
        }

        return plugin;
    }
}
