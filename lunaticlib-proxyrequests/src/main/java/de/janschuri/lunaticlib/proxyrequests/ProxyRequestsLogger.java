package de.janschuri.lunaticlib.proxyrequests;

public class ProxyRequestsLogger {
    public static void debug(String message) {
        LunaticProxyRequestsHandler.logger.debug(message);
    }

    public static void info(String message) {
        LunaticProxyRequestsHandler.logger.info(message);
    }

    public static void warn(String message) {
        LunaticProxyRequestsHandler.logger.warn(message);
    }

    public static void error(String message) {
        LunaticProxyRequestsHandler.logger.error(message);
    }
}
