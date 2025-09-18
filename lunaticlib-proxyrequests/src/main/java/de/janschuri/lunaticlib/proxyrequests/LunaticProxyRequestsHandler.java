package de.janschuri.lunaticlib.proxyrequests;

import com.google.common.io.ByteArrayDataInput;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.impl.LunaticLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LunaticProxyRequestsHandler {

    public static final String IDENTIFIER = "lunaticlib:proxyrequests";

    private static final Map<String, ProxyRequest> requests = new HashMap<>();

    private static ProxyRequestsAdapter adapter;

    private static final ProxyRequest[] defaultRequests = {
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

    static Logger logger = LunaticLogger.getLogger("LunaticLib-ProxyRequests");



    private LunaticProxyRequestsHandler() {}

    public static void enable(ProxyRequestsAdapter adapter) {
        if (LunaticProxyRequestsHandler.adapter == null) {
            synchronized (LunaticProxyRequestsHandler.class) {
                if (LunaticProxyRequestsHandler.adapter == null) {
                    LunaticProxyRequestsHandler.adapter = adapter;
                }
            }
        } else {
            throw new IllegalStateException("ProxyRequestsHandler already is enabled.");
        }

        registerDefaultRequests();
        logger.info("LunaticLib-ProxyRequests enabled.");
    }

    public static void disable() {
        shutdown();
        logger.info("LunaticLib-ProxyRequests disabled.");
    }

    public static ProxyRequestsAdapter adapter() {
        return adapter;
    }

    private static void registerDefaultRequests() {
        for (ProxyRequest request : defaultRequests) {
            LunaticProxyRequestsHandler.registerRequest(request);
        }
    }

    public static void handleRequest(String requestKey, ByteArrayDataInput in) {
        if (requests.containsKey(requestKey)) {
            CompletableFuture.runAsync(() -> {
                ProxyRequest request = requests.get(requestKey);
                if (request == null) {
                    ProxyRequestsLogger.error("Request not found: " + requestKey);
                    return;
                }
                request.execute(in);
            });
        }
    }

    public static void registerRequest(ProxyRequest request) {
        if (requests.containsKey(request.getRequestName())) {
            ProxyRequestsLogger.error("Request already registered: " + request.getRequestName());
            return;
        }

        requests.put(request.getRequestName(), request);
    }

    public static void unregisterRequest(String requestName) {
        requests.remove(requestName);
    }

    public static void shutdown() {
        List<ProxyRequest> tempRequests = new ArrayList<>(requests.values());
        for (ProxyRequest request : tempRequests) {
            request.unregister();
        }
        requests.clear();
    }
}
