package de.janschuri.lunaticlib.proxyrequests;

public class ProxyRequestsLogger {
    public static void debug(String message) {
        LunaticLibProxyRequests.logger.debug(message);
    }

    public static void info(String message) {
        LunaticLibProxyRequests.logger.info(message);
    }

    public static void warn(String message) {
        LunaticLibProxyRequests.logger.warn(message);
    }

    public static void error(String message) {
        LunaticLibProxyRequests.logger.error(message);
    }
}
