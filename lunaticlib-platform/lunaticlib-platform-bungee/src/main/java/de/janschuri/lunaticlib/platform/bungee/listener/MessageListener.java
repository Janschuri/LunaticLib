package de.janschuri.lunaticlib.platform.bungee.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

import static de.janschuri.lunaticlib.common.LunaticLib.IDENTIFIER;


public class MessageListener implements Listener {

    public void onPluginMessageReceived(PluginMessageEvent event) {

        if (!event.getTag().equals(IDENTIFIER)) {
            return;
        }

        byte[] message = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        Logger.debugLog("Received message on subchannel: " + subchannel);
        FutureRequestsHandler.handleRequest(subchannel, in);
    }
}
