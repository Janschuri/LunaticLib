package de.janschuri.lunaticlib.utils.logger;

public abstract class AbstractLogger {

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLUE = "\u001B[1m\u001B[34m";
    protected static final String ANSI_AQUA = "\u001B[1m\u001B[36m";

    public abstract void debug(String msg);

    public abstract void info(String msg);

    public abstract void warn(String msg);

    public abstract void error(String msg);
}
