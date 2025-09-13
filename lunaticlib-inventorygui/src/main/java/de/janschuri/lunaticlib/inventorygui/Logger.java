package de.janschuri.lunaticlib.inventorygui;

public class Logger {

    public static void debug(String message) {
        LunaticLibInventoryGui.logger.debug(message);
    }

    public static void info(String message) {
        LunaticLibInventoryGui.logger.info(message);
    }

    public static void warn(String message) {
        LunaticLibInventoryGui.logger.warn(message);
    }

    public static void error(String message) {
        LunaticLibInventoryGui.logger.error(message);
    }
}
