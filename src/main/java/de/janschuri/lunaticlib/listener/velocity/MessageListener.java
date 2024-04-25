package de.janschuri.lunaticlib.listener.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import de.janschuri.lunaticlib.utils.FutureRequests;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MessageListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        byte[] message = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("IsInRangeResponse")) {
            int requestId = in.readInt();
            boolean isInRange = in.readBoolean();
            CompletableFuture<Boolean> request = FutureRequests.booleanRequestMap.get(requestId);
            request.complete(isInRange);
        }
        if (subchannel.equals("HasItemInMainHandResponse")) {
            int requestId = in.readInt();
            boolean hasItem = in.readBoolean();
            CompletableFuture<Boolean> request = FutureRequests.booleanRequestMap.get(requestId);
            request.complete(hasItem);
        }
        if (subchannel.equals("GetItemInMainHandResponse")) {
            int requestId = in.readInt();
            byte[] item = new byte[in.readInt()];
            in.readFully(item);
            CompletableFuture<byte[]> request = FutureRequests.byteArrayRequestMap.get(requestId);
            request.complete(item);
        }
        if (subchannel.equals("RemoveItemInMainHandResponse")) {
            int requestId = in.readInt();
            boolean success = in.readBoolean();
            CompletableFuture<Boolean> request = FutureRequests.booleanRequestMap.get(requestId);
            request.complete(success);
        }
        if (subchannel.equals("GiveItemDropResponse")) {
            int requestId = in.readInt();
            boolean success = in.readBoolean();
            CompletableFuture<Boolean> request = FutureRequests.booleanRequestMap.get(requestId);
            request.complete(success);
        }
        if (subchannel.equals("GetPositionResponse")) {
            int requestId = in.readInt();
            double[] position = new double[3];
            position[0] = in.readDouble();
            position[1] = in.readDouble();
            position[2] = in.readDouble();
            CompletableFuture<double[]> request = FutureRequests.doubleArrayRequestMap.get(requestId);
            request.complete(position);
        }
        if (subchannel.equals("GetUniqueIdResponse")) {
            int requestId = in.readInt();
            UUID uuid = UUID.fromString(in.readUTF());
            CompletableFuture<UUID> request = FutureRequests.uuidRequestMap.get(requestId);
            request.complete(uuid);
        }
        if (subchannel.equals("GetNameResponse")) {
            int requestId = in.readInt();
            String name = in.readUTF();
            CompletableFuture<String> request = FutureRequests.stringRequestMap.get(requestId);
            request.complete(name);
        }
    }
}
