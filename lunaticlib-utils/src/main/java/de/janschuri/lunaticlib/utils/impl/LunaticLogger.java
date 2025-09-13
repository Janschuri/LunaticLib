package de.janschuri.lunaticlib.utils.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LunaticLogger implements de.janschuri.lunaticlib.utils.Logger {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[1m\u001B[34m";
    protected static final String ANSI_AQUA = "\u001B[1m\u001B[36m";
    protected static final String ANSI_RED = "\u001B[1m\u001B[31m";
    protected static final String ANSI_YELLOW = "\u001B[1m\u001B[33m";

    private final Logger logger;

    private LunaticLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg) {
        logger.info(ANSI_AQUA + msg + ANSI_RESET);
    }

    @Override
    public void info(String msg) {
        logger.info(ANSI_RESET + msg);
    }

    @Override
    public void warn(String msg) {
        logger.warn(ANSI_YELLOW + msg + ANSI_RESET);
    }

    @Override
    public void error(String msg) {
        logger.error(ANSI_RED + msg + ANSI_RESET);
    }

    public static LunaticLogger getLogger(String name) {
        return new LunaticLogger(LoggerFactory.getLogger(name));
    }
}
