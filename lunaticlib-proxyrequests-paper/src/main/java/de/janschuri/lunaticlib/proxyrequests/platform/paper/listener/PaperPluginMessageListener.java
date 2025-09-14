package de.janschuri.lunaticlib.proxyrequests.platform.paper.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.proxyrequests.handler.ProxyRequestsHandler;
import org.bukkit.plugin.messaging.PluginMessageListener;

import static de.janschuri.lunaticlib.proxyrequests.LunaticLibProxyRequests.IDENTIFIER;

public class PaperPluginMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player p, byte[] message) {

        if (!channel.equals(IDENTIFIER)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        ProxyRequestsHandler.handleRequest(subchannel, in);
    }
}
