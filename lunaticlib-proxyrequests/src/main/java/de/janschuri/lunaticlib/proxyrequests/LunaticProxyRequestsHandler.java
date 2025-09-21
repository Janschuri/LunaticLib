package de.janschuri.lunaticlib.proxyrequests;

import com.google.common.io.ByteArrayDataInput;
import de.janschuri.lunaticlib.proxyrequests.requests.*;
import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.LunaticLogger;
import de.janschuri.lunaticlib.utils.SingletonHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LunaticProxyRequestsHandler {

    public static final String IDENTIFIER = "lunaticlib:proxyrequests";

    private static final Map<String, ProxyRequest> requests = new HashMap<>();

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

    private static final SingletonHolder<ProxyRequestsAdapter> HOLDER = new SingletonHolder<>();

    public static void initialize(ProxyRequestsAdapter adapter) {
        HOLDER.initialize(adapter);
        registerDefaultRequests();
        logger.info("LunaticLib-ProxyRequests enabled.");
    }

    public static void shutdown() {
        List<ProxyRequest> tempRequests = new ArrayList<>(requests.values());
        for (ProxyRequest request : tempRequests) {
            request.unregister();
        }
        requests.clear();

        HOLDER.shutdown();
        logger.info("LunaticLib-ProxyRequests disabled.");
    }

    public static ProxyRequestsAdapter getAdapter() {
        return HOLDER.get();
    }

    public static boolean isEnabled() {
        return HOLDER.isInitialized();
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
}
