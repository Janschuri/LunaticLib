package de.janschuri.lunaticlib.platform.paper.inventorygui;

import de.janschuri.lunaticlib.platform.paper.inventorygui.handler.GUIManager;
import de.janschuri.lunaticlib.platform.paper.inventorygui.listener.GUIListener;
import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.LunaticLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperInventoryGUIHandler {

    private static JavaPlugin plugin;
    static Logger logger = LunaticLogger.getLogger("LunaticLib-InventoryGui");

    public static void initialize(JavaPlugin plugin) {
        PaperInventoryGUIHandler.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(new GUIListener(), getPluginInstance());
    }

    public static void shutdown() {
        GUIManager.closeAll();
        logger.info("LunaticLib-InventoryGui disabled.");
    }

    public static void logger(Logger logger) {
        PaperInventoryGUIHandler.logger = logger;
    }

    public static JavaPlugin getPluginInstance() {
        if (plugin == null) {
            throw new IllegalStateException("LunaticLibInventoryGui is not enabled. Please call LunaticLibInventoryGui.enable(plugin) first.");
        }

        return plugin;
    }
}
