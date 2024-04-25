package de.janschuri.lunaticlib.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.VelocityLunaticLib;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureRequests {

    private static final AtomicInteger requestIdGenerator = new AtomicInteger(0);
    private static final int timeout = 5;
    private static final TimeUnit unit = TimeUnit.SECONDS;

    public static final ConcurrentHashMap<Integer, CompletableFuture<Boolean>> booleanRequestMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, CompletableFuture<byte[]>> byteArrayRequestMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, CompletableFuture<double[]>> doubleArrayRequestMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, CompletableFuture<UUID>> uuidRequestMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, CompletableFuture<String>> stringRequestMap = new ConcurrentHashMap<>();


    public static UUID getUniqueId(String name) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<UUID> responseFuture = new CompletableFuture<>();

        uuidRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetUniqueIdRequest");
        out.writeInt(requestId);
        out.writeUTF(name);
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null; // An error occurred while waiting for response or timeout
        }
    }

    public static String getName(UUID uuid) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<String> responseFuture = new CompletableFuture<>();

        stringRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetNameRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null; // An error occurred while waiting for response or timeout
        }
    }

    public static boolean hasItemInMainHand(UUID uuid) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<Boolean> responseFuture = new CompletableFuture<>();

        booleanRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("HasItemInMainHandRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return false; // An error occurred while waiting for response or timeout
        }
    }

    public static byte[] getItemInMainHand(UUID uuid) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<byte[]> responseFuture = new CompletableFuture<>();

        byteArrayRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetItemInMainHandRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null; // An error occurred while waiting for response or timeout
        }
    }

    public static boolean removeItemInMainHand(UUID uuid) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<Boolean> responseFuture = new CompletableFuture<>();

        booleanRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RemoveItemInMainHandRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return false; // An error occurred while waiting for response or timeout
        }
    }

    public static boolean giveItemDrop(UUID uuid, byte[] item) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<Boolean> responseFuture = new CompletableFuture<>();

        booleanRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GiveItemDropRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        out.writeInt(item.length);
        out.write(item);
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return false; // An error occurred while waiting for response or timeout
        }
    }

    public static double[] getPosition(UUID uuid) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<double[]> responseFuture = new CompletableFuture<>();

        doubleArrayRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetPositionRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null; // An error occurred while waiting for response or timeout
        }
    }

    public static boolean isInRange(UUID uuid, UUID playerUUID, double range) {
        int requestId = requestIdGenerator.incrementAndGet();
        CompletableFuture<Boolean> responseFuture = new CompletableFuture<>();

        booleanRequestMap.put(requestId, responseFuture);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IsInRangeRequest");
        out.writeInt(requestId);
        out.writeUTF(uuid.toString());
        out.writeUTF(playerUUID.toString());
        out.writeDouble(range);
        VelocityLunaticLib.sendPluginMessage(out.toByteArray());

        try {
            // Wait for the response with a timeout
            return responseFuture.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return false; // An error occurred while waiting for response or timeout
        }
    }






}
