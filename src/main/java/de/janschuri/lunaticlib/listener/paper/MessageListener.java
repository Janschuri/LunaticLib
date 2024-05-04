package de.janschuri.lunaticlib.listener.paper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.futurerequests.FutureRequestsHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;

import static de.janschuri.lunaticlib.LunaticLib.IDENTIFIER;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player p, byte[] message) {

        if (!channel.equals(IDENTIFIER)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        FutureRequestsHandler.handleRequest(subchannel, in);
    }
}
