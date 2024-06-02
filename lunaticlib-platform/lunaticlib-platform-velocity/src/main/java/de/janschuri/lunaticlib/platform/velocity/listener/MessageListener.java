package de.janschuri.lunaticlib.platform.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.common.logger.Logger;

import static de.janschuri.lunaticlib.platform.velocity.VelocityLunaticLib.MINECRAFT_CHANNEL_IDENTIFIER;


public class MessageListener {

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
        Logger.debugLog("Received message on subchannel: " + subchannel);
        FutureRequestsHandler.handleRequest(subchannel, in);
    }
}
