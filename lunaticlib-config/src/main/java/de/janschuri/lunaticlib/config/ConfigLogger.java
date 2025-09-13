package de.janschuri.lunaticlib.config;

public class ConfigLogger {

    public static void debug(String message) {
        LunaticLibConfig.logger.debug(message);
    }

    public static void info(String message) {
        LunaticLibConfig.logger.info(message);
    }

    public static void warn(String message) {
        LunaticLibConfig.logger.warn(message);
    }

    public static void error(String message) {
        LunaticLibConfig.logger.error(message);
    }
}
