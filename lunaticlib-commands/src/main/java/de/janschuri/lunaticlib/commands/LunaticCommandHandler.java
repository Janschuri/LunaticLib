package de.janschuri.lunaticlib.commands;

import java.util.Objects;

public class LunaticCommandHandler {

    private LunaticCommandHandler(){}

    private static CommandAdapter<?> adapter;

    public static void enable(CommandAdapter<?> adapter) {
        if (LunaticCommandHandler.adapter == null) {
            synchronized (LunaticCommandHandler.class) {
                if (LunaticCommandHandler.adapter == null) {
                    LunaticCommandHandler.adapter = adapter;
                }
            }
        } else {
            throw new IllegalStateException("LunaticCommandHandler already is enabled.");
        }
    }

    public static CommandAdapter<?> adapter() {
        return Objects.requireNonNull(adapter, "LunaticCommandHandler not enabled. Call LunaticCommandHandler.enable(adapter) first.");
    }
}
