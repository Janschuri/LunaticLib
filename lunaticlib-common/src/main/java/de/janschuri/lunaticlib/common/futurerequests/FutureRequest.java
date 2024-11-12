package de.janschuri.lunaticlib.common.futurerequests;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FutureRequest<R> {

    protected final String requestName;
    protected final boolean suppressTimeoutException;
    protected final ConcurrentHashMap<Integer, CompletableFuture<R>> requestMap;
    protected static final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    protected final int timeout;
    protected static final TimeUnit UNIT = TimeUnit.SECONDS;
    protected static final String RESPONSE = "Response";
    protected static final String REQUEST = "Request";

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
        this.suppressTimeoutException = false;
        this.timeout = 3;
    }

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP, boolean suppressTimeoutException) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
        this.suppressTimeoutException = suppressTimeoutException;
        this.timeout = 3;
    }

    public FutureRequest(String REQUEST_NAME, ConcurrentHashMap<Integer, CompletableFuture<R>> REQUEST_MAP, int timeout) {
        this.requestName = REQUEST_NAME;
        this.requestMap = REQUEST_MAP;
        this.suppressTimeoutException = false;
        this.timeout = timeout;
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
        return sendRequest(data, false);
    }

    protected R sendRequest(byte[] data, boolean voidRequest) {
        CompletableFuture<R> responseFuture = new CompletableFuture<>();
        int requestId = requestIdGenerator.incrementAndGet();
        requestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(REQUEST);
        out.writeInt(requestId);
        out.write(data);

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String stackTrace = "";

        for (int i = 0; i < stackTraceElements.length; i++) {
            stackTrace += " to " + stackTraceElements[i].getClassName() + " in " + stackTraceElements[i].getMethodName() + " at " + stackTraceElements[i].getLineNumber() + "\n";
        }

        Logger.debugLog("Sending request: " + requestName + " with id: " + requestId + " from " + stackTrace);

        if (!LunaticLib.getPlatform().sendPluginMessage(out.toByteArray())) {
            Logger.debugLog("Cannot sent plugin message: " + requestName + "-Request" + " with id: " + requestId);
            return null;
        }

        try {
            if (voidRequest) {
                return null;
            }
            return responseFuture.get(timeout, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (!suppressTimeoutException) {
                Logger.errorLog("Error while waiting for response: " + requestName + " with id: " + requestId);
            }
            return null;
        }
    }

    protected CompletableFuture<R> sendRequestAsync(byte[] data) {
        CompletableFuture<R> responseFuture = new CompletableFuture<>();
        int requestId = requestIdGenerator.incrementAndGet();
        requestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(REQUEST);
        out.writeInt(requestId);
        out.write(data);

        // Logging
        Logger.debugLog("Sending request: " + requestName + " with id: " + requestId + " from " + getStackTrace());

        if (!LunaticLib.getPlatform().sendPluginMessage(out.toByteArray())) {
            Logger.debugLog("Cannot send plugin message: " + requestName + "-Request with id: " + requestId);
            responseFuture.completeExceptionally(new RuntimeException("Failed to send plugin message"));
        }

        // Set timeout
        return responseFuture.orTimeout(timeout, UNIT).whenComplete((result, throwable) -> {
            if (throwable != null) {
                if (!suppressTimeoutException) {
                    Logger.errorLog("Error while waiting for response: " + requestName + " with id: " + requestId);
                }
                requestMap.remove(requestId);
            }
        });
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


        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        String stackTrace = "";

        for (int i = 0; i < stackTraceElements.length || i < 5 ; i++) {
            stackTrace += " to " + stackTraceElements[i].getClassName() + " in " + stackTraceElements[i].getMethodName() + " at " + stackTraceElements[i].getLineNumber() + "\n";
        }

        Logger.debugLog("Sending request: " + requestName + " with id: " + requestId + " from " + stackTrace);

        if (!LunaticLib.getPlatform().sendPluginMessage(serverName, out.toByteArray())) {
            Logger.debugLog("Cannot sent plugin message: " + requestName + "-Request to " + serverName + " with id: " + requestId);
            return null;
        }

        try {
            return responseFuture.get(timeout, UNIT);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            if (!suppressTimeoutException) {
                Logger.errorLog("Error while waiting for response: " + requestName + " to " + serverName + " with id: " + requestId);
            }
            return null;
        }
    }

    protected boolean sendResponse(int requestId, byte[] data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestName);
        out.writeUTF(RESPONSE);
        out.writeInt(requestId);
        out.write(data);

        Logger.debugLog("Sending response: " + requestName + " with id: " + requestId);

        return LunaticLib.getPlatform().sendPluginMessage(out.toByteArray());
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

    private String getStackTrace() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stackTrace = new StringBuilder();
        for (int i = 0; i < stackTraceElements.length && i < 5; i++) {
            stackTrace.append(" to ").append(stackTraceElements[i].getClassName())
                    .append(" in ").append(stackTraceElements[i].getMethodName())
                    .append(" at ").append(stackTraceElements[i].getLineNumber())
                    .append("\n");
        }
        return stackTrace.toString();
    }
}
