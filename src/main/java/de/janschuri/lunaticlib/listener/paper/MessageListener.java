package de.janschuri.lunaticlib.listener.paper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.senders.paper.PlayerSender;
import de.janschuri.lunaticlib.utils.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player p, byte[] message) {

        if (!channel.equals("lunaticlib:proxy")) {
            return;
        }


        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        Logger.debugLog("plugin message received: " + subchannel);

        if (subchannel.equals("IsInRangeRequest")) {
            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());
            UUID partnerUUID = UUID.fromString(in.readUTF());
            double range = in.readDouble();

            PlayerSender player = new PlayerSender(playerUUID);
            PlayerSender partner = new PlayerSender(partnerUUID);
            if (!player.isOnline() || !partner.isOnline()) {
                return;
            }

            boolean isInRange = player.isInRange(partnerUUID, range);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("IsInRangeResponse");
            out.writeInt(requestId);
            out.writeBoolean(isInRange);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("HasItemInMainHandRequest")) {

            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());

            if (Bukkit.getPlayer(playerUUID) == null) {
                return;
            }

            PlayerSender player = new PlayerSender(playerUUID);

            boolean hasItemInMainHand = player.hasItemInMainHand();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("HasItemInMainHandResponse");
            out.writeInt(requestId);
            out.writeBoolean(hasItemInMainHand);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("GetItemInMainHandRequest")) {
            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());

            if (Bukkit.getPlayer(playerUUID) == null) {
                return;
            }

            PlayerSender player = new PlayerSender(playerUUID);

            byte[] item = player.getItemInMainHand();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetItemInMainHandResponse");
            out.writeInt(requestId);
            out.writeInt(item.length);
            out.write(item);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("RemoveItemInMainHandRequest")) {
            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());

            if (Bukkit.getPlayer(playerUUID) == null) {
                return;
            }

            PlayerSender player = new PlayerSender(playerUUID);

            boolean removed = player.removeItemInMainHand();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("RemoveItemInMainHandResponse");
            out.writeInt(requestId);
            out.writeBoolean(removed);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("GiveItemDropRequest")) {
            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());

            if (Bukkit.getPlayer(playerUUID) == null) {
                return;
            }

            byte[] item = new byte[in.readInt()];
            in.readFully(item);

            PlayerSender player = new PlayerSender(playerUUID);

            boolean dropped = player.giveItemDrop(item);

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GiveItemDropResponse");
            out.writeInt(requestId);
            out.writeBoolean(dropped);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("GetPositionRequest")) {
            int requestId = in.readInt();
            UUID playerUUID = UUID.fromString(in.readUTF());

            if (Bukkit.getPlayer(playerUUID) == null) {
                return;
            }

            PlayerSender player = new PlayerSender(playerUUID);

            double[] position = player.getPosition();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetPositionResponse");
            out.writeInt(requestId);
            out.writeDouble(position[0]);
            out.writeDouble(position[1]);
            out.writeDouble(position[2]);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("GetUniqueIdRequest")) {
            int requestId = in.readInt();
            String name = in.readUTF();

            PlayerSender player = new PlayerSender(name);
            UUID uuid = player.getUniqueId();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetUniqueIdResponse");
            out.writeInt(requestId);
            out.writeUTF(uuid.toString());

            LunaticLib.sendPluginMessage(out.toByteArray());
        }

        if (subchannel.equals("GetNameRequest")) {
            int requestId = in.readInt();
            UUID uuid = UUID.fromString(in.readUTF());

            PlayerSender player = new PlayerSender(uuid);
            String name = player.getName();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("GetNameResponse");
            out.writeInt(requestId);
            out.writeUTF(name);

            LunaticLib.sendPluginMessage(out.toByteArray());
        }
    }
}
