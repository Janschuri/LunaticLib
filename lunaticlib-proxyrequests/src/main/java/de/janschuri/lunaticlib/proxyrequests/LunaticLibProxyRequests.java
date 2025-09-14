package de.janschuri.lunaticlib.proxyrequests;

import de.janschuri.lunaticlib.proxyrequests.handler.ProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.impl.LunaticLogger;

public class LunaticLibProxyRequests {

    public static final String IDENTIFIER = "lunaticlib:proxyrequests";

    private LunaticLibProxyRequests() {}

    static Logger logger = LunaticLogger.getLogger("LunaticLib-ProxyRequests");
    private static ProxyRequestsPlatform platform;

    public static void enable() {
        registerRequests();
        logger.info("LunaticLib-ProxyRequests enabled.");
    }

    public static void disable() {
        ProxyRequestsHandler.shutdown();
        logger.info("LunaticLib-ProxyRequests disabled.");
    }

    protected static void setPlatform(ProxyRequestsPlatform platform) {
        LunaticLibProxyRequests.platform = platform;
    }

    public static ProxyRequestsPlatform getPlatform() {
        return platform;
    }

    private static final ProxyRequest[] requests = {
            new GetItemInMainHandRequest(),
            new GetPositionRequest(),
            new GiveItemDropRequest(),
            new HasItemInMainHandRequest(),
            new IsInRangeRequest(),
            new RemoveItemInMainHandRequest(),
            new GetSkinURLRequest(),
            new HasEnoughMoneyRequest(),
            new WithdrawMoneyRequest(),
            new OpenDecisionGUIRequest(),
            new RunCommandRequest(),
    };

    private static void registerRequests() {
        for (ProxyRequest request : requests) {
            ProxyRequestsHandler.registerRequest(request);
        }
    }
}
