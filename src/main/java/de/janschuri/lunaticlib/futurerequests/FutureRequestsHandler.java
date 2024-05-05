package de.janschuri.lunaticlib.futurerequests;

import com.google.common.io.ByteArrayDataInput;
import de.janschuri.lunaticlib.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FutureRequestsHandler {

    private static final Map<String, FutureRequest> requests = new HashMap<>();

    public static void handleRequest(String requestKey, ByteArrayDataInput in) {
        if (requests.containsKey(requestKey)) {
            Logger.debugLog("Handling request: " + requestKey);
            requests.get(requestKey).execute(in);
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
