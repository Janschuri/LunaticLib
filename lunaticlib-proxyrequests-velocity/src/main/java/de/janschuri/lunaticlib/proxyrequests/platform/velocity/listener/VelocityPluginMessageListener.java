package de.janschuri.lunaticlib.proxyrequests.platform.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import de.janschuri.lunaticlib.proxyrequests.handler.ProxyRequestsHandler;

import static de.janschuri.lunaticlib.proxyrequests.platform.velocity.VelocityLunaticLibProxyRequests.MINECRAFT_CHANNEL_IDENTIFIER;

public class VelocityPluginMessageListener {


    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }
        if (event.getIdentifier() != MINECRAFT_CHANNEL_IDENTIFIER) {
            return;
        }

        byte[] message = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        ProxyRequestsHandler.handleRequest(subchannel, in);
    }
}
