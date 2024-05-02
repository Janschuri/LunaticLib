package de.janschuri.lunaticlib.logger;


import de.janschuri.lunaticlib.LunaticLib;

public class Logger extends AbstractLogger {

    private static org.slf4j.Logger logger = AbstractLogger.getLogger("LunaticLib");

    public boolean isDebug() {
        return LunaticLib.isDebug;
    }

    public static void debugLog(String msg) {
        debug(logger, msg);
    }

    public static void infoLog(String msg) {
        info(logger, msg);
    }

    public static void warnLog(String msg) {
        warn(logger, msg);
    }

    public static void errorLog(String msg) {
        error(logger, msg);
    }
}


