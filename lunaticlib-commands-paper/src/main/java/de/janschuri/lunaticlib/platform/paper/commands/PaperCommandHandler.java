package de.janschuri.lunaticlib.platform.paper.commands;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.CommandAdapter;
import de.janschuri.lunaticlib.commands.LunaticCommandHandler;
import de.janschuri.lunaticlib.sender.Sender;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderHandler;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class PaperCommandHandler {

    private static volatile CommandAdapter adapter;

    public static void enable(JavaPlugin plugin) {
        if (adapter == null) {
            synchronized (PaperCommandHandler.class) {
                if (adapter == null) {
                    adapter = new PaperCommandAdapter();
                    LunaticCommandHandler.enable(adapter);
                }
            }

            return;
        }

        throw new IllegalStateException("LunaticLibCommands already is enabled.");
    }

    public static CommandAdapter adapter() {
        return Objects.requireNonNull(adapter,"LunaticLibCommands not enabled. Call PaperLunaticLibCommands.enable(plugin) first.");
    }

    public static class PaperCommand implements CommandExecutor, TabCompleter {

        private final Command command;

        public PaperCommand(Command command) {
            this.command = command;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            Sender commandSender = PaperSenderHandler.adapter().getSender(sender);
            return this.command.checkAndExecute(commandSender, args);
        }

        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            String[] newArgs = new String[args.length + 1];
            newArgs[0] = this.command.getName();
            System.arraycopy(args, 0, newArgs, 1, args.length);
            Sender commandSender = PaperSenderHandler.adapter().getSender(sender);
            return this.command.tabComplete(commandSender, newArgs);
        }
    }
}