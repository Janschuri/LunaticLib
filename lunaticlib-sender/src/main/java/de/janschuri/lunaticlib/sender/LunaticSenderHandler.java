package de.janschuri.lunaticlib.sender;

import java.util.Objects;

public class LunaticSenderHandler {

    private LunaticSenderHandler(){}

    private static SenderAdapter<?, ?> adapter;

    public static void enable(SenderAdapter<?, ?> adapter) {
        if (LunaticSenderHandler.adapter == null) {
            synchronized (LunaticSenderHandler.class) {
                if (LunaticSenderHandler.adapter == null) {
                    LunaticSenderHandler.adapter = adapter;
                }
            }
        } else {
            throw new IllegalStateException("LunaticCommandHandler already is enabled.");
        }
    }

    public static SenderAdapter<?, ?> adapter() {
        return Objects.requireNonNull(adapter, "LunaticCommandHandler not enabled. Call LunaticCommandHandler.enable(adapter) first.");
    }
}
