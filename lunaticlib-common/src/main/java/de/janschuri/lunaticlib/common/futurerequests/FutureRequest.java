package de.janschuri.lunaticlib.common.futurerequests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;
import de.janschuri.lunaticlib.common.utils.Utils;
import jdk.jshell.execution.Util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FutureRequest<R> {

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

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
    }
    protected FutureRequest<R> timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    protected FutureRequest<R> suppressTimeoutException() {
        this.suppressTimeoutException = true;
        return this;
    }

    protected FutureRequest<R> isVoid() {
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
            if (!LunaticLib.getPlatform().sendPluginMessage(serverName, out.toByteArray())) {
                responseFuture.completeExceptionally(new RuntimeException("Failed to send plugin message"));
            }
        } else {
            if (!LunaticLib.getPlatform().sendPluginMessage(out.toByteArray())) {
                responseFuture.completeExceptionally(new RuntimeException("Failed to send plugin message"));
            }
        }

        if (isVoid) {
            return responseFuture.thenApply(result -> null);
        }

        return responseFuture.orTimeout(timeout, UNIT).whenComplete((result, throwable) -> {
            if (throwable != null) {
                if (!suppressTimeoutException) {
                    Logger.errorLog("Error while waiting for response: " + requestName + " with id: " + requestId);
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

        return LunaticLib.getPlatform().sendPluginMessage(out.toByteArray());
    }

    protected void completeRequest(int requestId, R response) {
        CompletableFuture<R> future = requestMap.get(requestId);
        if (future != null) {
            future.complete(response);
            requestMap.remove(requestId);
        }
    }

    protected void unregister() {
        FutureRequestsHandler.unregisterRequest(requestName);
        for (CompletableFuture<R> future : requestMap.values()) {
            future.complete(null);
        }
        requestMap.clear();
    }
}
