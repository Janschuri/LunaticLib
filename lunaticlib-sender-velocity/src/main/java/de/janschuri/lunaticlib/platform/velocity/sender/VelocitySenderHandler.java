package de.janschuri.lunaticlib.platform.velocity.sender;

import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.sender.LunaticSenderHandler;
import de.janschuri.lunaticlib.sender.SenderAdapter;

import java.util.Objects;

public final class VelocitySenderHandler {

    private static volatile VelocitySenderAdapter adapter;

    public static void enable(ProxyServer proxy) {
        if (adapter == null) {
            synchronized (VelocitySenderHandler.class) {
                if (adapter == null) {
                    adapter = new VelocitySenderAdapter(proxy);
                    LunaticSenderHandler.enable(adapter);
                }
            }
        }
    }

    public static SenderAdapter adapter() {
        return Objects.requireNonNull(adapter,"LunaticLibSender not enabled. Call PaperLunaticLibSender.enable() first.");
    }
}