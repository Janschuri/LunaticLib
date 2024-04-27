package de.janschuri.lunaticlib.utils.logger;

import org.slf4j.Logger;

public class VelocityLogger extends AbstractLogger {

    private final Logger logger;

    public VelocityLogger(Logger logger) {
        this.logger = logger;
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void error(String msg) {
        logger.error(msg);
    }
}
