package de.janschuri.lunaticlib.platform.paper.inventorygui;

public class Logger {

    public static void debug(String message) {
        PaperInventoryGUIHandler.logger.debug(message);
    }

    public static void info(String message) {
        PaperInventoryGUIHandler.logger.info(message);
    }

    public static void warn(String message) {
        PaperInventoryGUIHandler.logger.warn(message);
    }

    public static void error(String message) {
        PaperInventoryGUIHandler.logger.error(message);
    }
}
