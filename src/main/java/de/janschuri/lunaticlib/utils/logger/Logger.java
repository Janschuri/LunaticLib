package de.janschuri.lunaticlib.utils.logger;


import de.janschuri.lunaticlib.LunaticLib;

public class Logger {

    private static AbstractLogger logger;

    public Logger(AbstractLogger logger) {
        Logger.logger = logger;
    }

    public boolean isDebug() {
        return LunaticLib.isDebug;
    }

    public static void debugLog(String msg) {
        logger.debug(msg);
    }

    public static void infoLog(String msg) {
        logger.info(msg);
    }

    public static void warnLog(String msg) {
        logger.warn(msg);
    }

    public static void errorLog(String msg) {
        logger.error(msg);
    }
}


