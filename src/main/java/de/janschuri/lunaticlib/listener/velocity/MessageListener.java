package de.janschuri.lunaticlib.listener.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import de.janschuri.lunaticlib.futurerequests.FutureRequestsHandler;

import static de.janschuri.lunaticlib.VelocityLunaticLib.IDENTIFIER;

public class MessageListener {

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }
        if (event.getIdentifier() != IDENTIFIER) {
            return;
        }

        byte[] message = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        FutureRequestsHandler.handleRequest(subchannel, in);
    }
}
