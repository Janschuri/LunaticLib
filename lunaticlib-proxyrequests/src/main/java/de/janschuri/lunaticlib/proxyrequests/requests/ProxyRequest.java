package de.janschuri.lunaticlib.proxyrequests.requests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.LunaticProxyRequestsHandler;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;
import de.janschuri.lunaticlib.utils.Utils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ProxyRequest<R> {

    protected final String requestName;
    protected boolean suppressTimeoutException = false;
    protected final ConcurrentHashMap<Integer, CompletableFuture<R>> requestMap;
    protected static final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    protected int timeout = 3;
    protected boolean isVoid = false;
    protected static final TimeUnit UNIT = TimeUnit.SECONDS;
    protected static final String RESPONSE = "Response";
    protected static final String REQUEST = "Request";

    private static Set<Integer> currentRequests = new ConcurrentSkipListSet<>();

    public ProxyRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
    }
    protected ProxyRequest<R> timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    protected ProxyRequest<R> suppressTimeoutException() {
        this.suppressTimeoutException = true;
        return this;
    }

    protected ProxyRequest<R> isVoid() {
        this.isVoid = true;
        return this;
    }

    public void execute(ByteArrayDataInput in) {
        String type = in.readUTF();
        int requestId = in.readInt();

        if (type.equals(REQUEST)) {

            if (!currentRequests.add(requestId)) {
                return;
            }

            Utils.scheduleTask(() -> {
                currentRequests.remove(Integer.valueOf(requestId));
            }, 1, TimeUnit.SECONDS);

            handleRequest(requestId, in);
        } else if (type.equals(RESPONSE)) {
            handleResponse(requestId, in);
        } else {
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    public String getRequestName() {
        return requestName;
    }

    protected abstract void handleRequest(int requestId, ByteArrayDataInput in);

    protected abstract void handleResponse(int requestId, ByteArrayDataInput in);

    protected CompletableFuture<R> sendRequest(byte[] data) {
        return sendRequest(null, data);
    }

    protected CompletableFuture<R> sendRequest(String serverName, byte[] data) {
        CompletableFuture<R> responseFuture = new CompletableFuture<>();
        int requestId = requestIdGenerator.incrementAndGet();
        requestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(REQUEST);
        out.writeInt(requestId);
        out.write(data);

        if (serverName != null) {
            if (!LunaticProxyRequestsHandler.getAdapter().sendPluginMessage(serverName, out.toByteArray())) {
                responseFuture.completeExceptionally(new RuntimeException("Failed to send plugin message"));
            }
        } else {
            if (!LunaticProxyRequestsHandler.getAdapter().sendPluginMessage(out.toByteArray())) {
                responseFuture.completeExceptionally(new RuntimeException("Failed to send plugin message"));
            }
        }

        if (isVoid) {
            return responseFuture.thenApply(result -> null);
        }

        return responseFuture.orTimeout(timeout, UNIT).whenComplete((result, throwable) -> {
            if (throwable != null) {
                if (!suppressTimeoutException) {
                    ProxyRequestsLogger.error("Error while waiting for response: " + requestName + " with id: " + requestId);
                }
                requestMap.remove(requestId);
            }
        });
    }


    protected boolean sendResponse(int requestId, byte[] data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(RESPONSE);
        out.writeInt(requestId);
        out.write(data);

        return LunaticProxyRequestsHandler.getAdapter().sendPluginMessage(out.toByteArray());
    }

    protected void completeRequest(int requestId, R response) {
        CompletableFuture<R> future = requestMap.get(requestId);
        if (future != null) {
            future.complete(response);
            requestMap.remove(requestId);
        }
    }

    public void unregister() {
        LunaticProxyRequestsHandler.unregisterRequest(requestName);
        for (CompletableFuture<R> future : requestMap.values()) {
            future.complete(null);
        }
        requestMap.clear();
    }
}
