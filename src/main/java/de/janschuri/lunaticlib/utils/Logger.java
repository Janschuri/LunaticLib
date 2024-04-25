package de.janschuri.lunaticlib.utils;


public abstract class Logger {

    private static Logger logger;
    public static boolean isDebug = false;

    public static void loadLogger(Logger logger) {
        Logger.logger = logger;
    }

    public static void debugLog(String msg) {
        if(isDebug){
            logger.debugLog(msg);
        }
    }

    public static void infoLog(String msg) {
        logger.infoLog(msg);
    }

    public static void warnLog(String msg) {
        logger.warnLog(msg);
    }

    public static void errorLog(String msg) {
        logger.errorLog(msg);
    }
}
