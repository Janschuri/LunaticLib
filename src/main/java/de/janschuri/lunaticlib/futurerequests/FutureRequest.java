package de.janschuri.lunaticlib.futurerequests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.logger.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FutureRequest<R> {

    protected final String requestName;
    protected boolean suppressTimeoutException = false;
    protected final ConcurrentHashMap<Integer, CompletableFuture<R>> requestMap;
    protected static final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    protected static final int TIMEOUT = 3;
    protected static final TimeUnit UNIT = TimeUnit.SECONDS;
    protected static final String RESPONSE = "Response";
    protected static final String REQUEST = "Request";

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
    }

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP, boolean suppressTimeoutException) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
        this.suppressTimeoutException = suppressTimeoutException;
    }

    public void execute(ByteArrayDataInput in) {
        String type = in.readUTF();
        int requestId = in.readInt();

        if (type.equals(REQUEST)) {
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

    protected R sendRequest(byte[] data) {
        CompletableFuture<R> responseFuture = new CompletableFuture<>();
        int requestId = requestIdGenerator.incrementAndGet();
        requestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(REQUEST);
        out.writeInt(requestId);
        out.write(data);


        Logger.debugLog("Sending request: " + requestName + " with id: " + requestId);

        if (!LunaticLib.sendPluginMessage(out.toByteArray())) {
            Logger.debugLog("Cannot sent plugin message: " + requestName + "-Request" + " with id: " + requestId);
            return null;
        }

        try {
            return responseFuture.get(TIMEOUT, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (!suppressTimeoutException) {
                Logger.errorLog("Error while waiting for response: " + requestName + " with id: " + requestId);
            }
            return null;
        }
    }

    protected R sendRequest(String serverName, byte[] data) {
        CompletableFuture<R> responseFuture = new CompletableFuture<>();
        int requestId = requestIdGenerator.incrementAndGet();
        requestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(REQUEST);
        out.writeInt(requestId);
        out.write(data);


        Logger.debugLog("Sending request: " + requestName + " to " + serverName + "with id: " + requestId);

        if (!LunaticLib.sendPluginMessage(serverName, out.toByteArray())) {
            Logger.debugLog("Cannot sent plugin message: " + requestName + "-Request to " + serverName + " with id: " + requestId);
            return null;
        }

        try {
            return responseFuture.get(TIMEOUT, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (!suppressTimeoutException) {
                Logger.errorLog("Error while waiting for response: " + requestName + " to " + serverName + " with id: " + requestId);
            }
            return null;
        }
    }

    protected void sendResponse(int requestId, byte[] data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(RESPONSE);
        out.writeInt(requestId);
        out.write(data);

        Logger.debugLog("Sending response: " + requestName + " with id: " + requestId);

        LunaticLib.sendPluginMessage(out.toByteArray());
    }

    protected void completeRequest(int requestId, R response) {
        Logger.debugLog("Completing request: " + requestName + " with id: " + requestId);
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
