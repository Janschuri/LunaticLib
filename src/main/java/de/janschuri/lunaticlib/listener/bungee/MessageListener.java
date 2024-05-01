package de.janschuri.lunaticlib.listener.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.janschuri.lunaticlib.LunaticLib;
import de.janschuri.lunaticlib.futurerequests.FutureRequestsHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

import static de.janschuri.lunaticlib.LunaticLib.IDENTIFIER;

public class MessageListener implements Listener {

    public void onPluginMessageReceived(PluginMessageEvent event) {

        if (!event.getTag().equals(IDENTIFIER)) {
            return;
        }

        String serverName = ((ProxiedPlayer) event.getSender()).getServer().getInfo().getName();
        byte[] message = event.getData();
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        FutureRequestsHandler.handleRequest(subchannel, in);
    }
}
