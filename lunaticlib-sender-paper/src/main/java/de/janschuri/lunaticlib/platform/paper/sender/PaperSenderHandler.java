package de.janschuri.lunaticlib.platform.paper.sender;

import de.janschuri.lunaticlib.sender.LunaticSenderHandler;
import de.janschuri.lunaticlib.sender.SenderAdapter;

import java.util.Objects;

public final class PaperSenderHandler {

    private static volatile PaperSenderAdapter adapter;

    public static void enable() {
        if (adapter == null) {
            synchronized (PaperSenderHandler.class) {
                if (adapter == null) {
                    adapter = new PaperSenderAdapter();
                    LunaticSenderHandler.enable(adapter);
                }
            }
        }
    }

    public static SenderAdapter adapter() {
        return Objects.requireNonNull(adapter,"LunaticLibSender not enabled. Call PaperLunaticLibSender.enable() first.");
    }
}