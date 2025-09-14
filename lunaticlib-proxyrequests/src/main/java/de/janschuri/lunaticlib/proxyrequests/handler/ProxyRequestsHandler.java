package de.janschuri.lunaticlib.proxyrequests.handler;

import com.google.common.io.ByteArrayDataInput;
import de.janschuri.lunaticlib.proxyrequests.requests.ProxyRequest;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ProxyRequestsHandler {

    private static final Map<String, ProxyRequest> requests = new HashMap<>();

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
