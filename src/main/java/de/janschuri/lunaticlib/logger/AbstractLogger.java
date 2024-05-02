package de.janschuri.lunaticlib.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLogger {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[1m\u001B[34m";
    protected static final String ANSI_AQUA = "\u001B[1m\u001B[36m";
    protected static final String ANSI_RED = "\u001B[1m\u001B[31m";
    protected static final String ANSI_YELLOW = "\u001B[1m\u001B[33m";


    protected static void debug(Logger logger, String msg) {
        logger.debug(ANSI_AQUA + msg + ANSI_RESET);
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

    abstract public boolean isDebug();

    protected static Logger getLogger(String pluginName) {
        return LoggerFactory.getLogger(pluginName);
    }
}
