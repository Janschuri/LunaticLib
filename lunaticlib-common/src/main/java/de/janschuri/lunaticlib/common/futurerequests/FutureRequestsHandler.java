package de.janschuri.lunaticlib.common.futurerequests;

import com.google.common.io.ByteArrayDataInput;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FutureRequestsHandler {

    private static final Map<String, FutureRequest> requests = new HashMap<>();

    public static void handleRequest(String requestKey, ByteArrayDataInput in) {
        Logger.debugLog(String.format("Handling request: %s", requestKey));
        if (requests.containsKey(requestKey)) {
            Logger.debugLog(String.format("Request %s already exists", requestKey));
            CompletableFuture.runAsync(() -> {
                FutureRequest request = requests.get(requestKey);

                Logger.debugLog(String.format("Executing request %s", requestKey));

                for (FutureRequest r : requests.values()) {
                    Logger.debugLog(String.format("Request %s is registered", r.getRequestName()));
                }

                request.execute(in);
            });
        } else {
            Logger.errorLog("Unknown request: " + requestKey);
        }
    }

    public static void registerRequest(FutureRequest request) {
        if (requests.containsKey(request.getRequestName())) {
            Logger.errorLog("Request already registered: " + request.getRequestName());
            return;
        }

        requests.put(request.getRequestName(), request);
    }

    public static void unregisterRequest(String requestName) {
        requests.remove(requestName);
    }

    public static void shutdown() {
        List<FutureRequest> tempRequests = new ArrayList<>(requests.values());
        for (FutureRequest request : tempRequests) {
            request.unregister();
        }
        requests.clear();
    }
}
