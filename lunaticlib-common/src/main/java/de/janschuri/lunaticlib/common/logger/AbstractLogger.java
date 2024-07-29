package de.janschuri.lunaticlib.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLogger {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[1m\u001B[34m";
    protected static final String ANSI_AQUA = "\u001B[1m\u001B[36m";
    protected static final String ANSI_RED = "\u001B[1m\u001B[31m";
    protected static final String ANSI_YELLOW = "\u001B[1m\u001B[33m";

    private static final Map<String, Logger> loggerMap = new HashMap<>();


    protected static void debug(Logger logger, String msg) {
        logger.info(ANSI_AQUA + msg + ANSI_RESET);
    }

    protected static void info(Logger logger, String msg) {
        logger.info(ANSI_RESET + msg);
    }

    protected static void warn(Logger logger, String msg) {
        logger.warn(ANSI_YELLOW + msg + ANSI_RESET);
    }

    protected static void error(Logger logger, String msg) {
        logger.error(ANSI_RED + msg + ANSI_RESET);
    }

    public static void debug(String pluginName, String msg) {
        createLogger(pluginName);
        loggerMap.get(pluginName).info(ANSI_AQUA + msg + ANSI_RESET);
    }

    public static void info(String pluginName, String msg) {
        createLogger(pluginName);
        loggerMap.get(pluginName).info(ANSI_RESET + msg);
    }

    public static void warn(String pluginName, String msg) {
        createLogger(pluginName);
        loggerMap.get(pluginName).warn(ANSI_YELLOW + msg + ANSI_RESET);
    }

    public static void error(String pluginName, String msg) {
        createLogger(pluginName);
        loggerMap.get(pluginName).error(ANSI_RED + msg + ANSI_RESET);
    }

    protected static Logger getLogger(String pluginName) {
        return LoggerFactory.getLogger(pluginName);
    }

    private static void createLogger(String pluginName) {
        if (!loggerMap.containsKey(pluginName)) {
            loggerMap.put(pluginName, getLogger(pluginName));
        }
    }
}
