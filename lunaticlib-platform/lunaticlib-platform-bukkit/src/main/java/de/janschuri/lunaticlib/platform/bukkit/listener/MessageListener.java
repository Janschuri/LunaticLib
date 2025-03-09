package de.janschuri.lunaticlib.platform.bukkit.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.common.logger.Logger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;

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
