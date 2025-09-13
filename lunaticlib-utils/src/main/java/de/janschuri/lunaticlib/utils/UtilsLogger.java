package de.janschuri.lunaticlib.utils;

public class UtilsLogger {

    private UtilsLogger() {}

    public static void debug(String message) {
        Utils.logger.debug(message);
    }

    public static void info(String message) {
        Utils.logger.info(message);
    }

    public static void warn(String message) {
        Utils.logger.warn(message);
    }

    public static void error(String message) {
        Utils.logger.error(message);
    }
}
