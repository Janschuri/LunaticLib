package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.impl.LunaticLogger;

public class ConfigLogger {

    static Logger logger = LunaticLogger.getLogger("LunaticLib-Config");

    public static void logger(Logger logger) {
        ConfigLogger.logger = logger;
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }
}
