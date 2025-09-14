package de.janschuri.lunaticlib.commands.platform.paper;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.LunaticLibCommands;
import de.janschuri.lunaticlib.commands.platform.paper.impl.PlatformImpl;
import de.janschuri.lunaticlib.sender.Sender;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PaperLunaticLibCommands extends LunaticLibCommands {

    private static PlatformImpl platform;

    public static void enable() {
        if (getPlatform() == null) {
            platform = new PlatformImpl();
            setPlatform(platform);
        }
    }

    public static PlatformImpl getPlatformImpl() {
        if (platform == null) {
            throw new IllegalStateException("LunaticCommands platform is not set. Please call LunaticCommands.enable() first.");
        }

        return platform;
    }

    public static class PaperCommand implements CommandExecutor, TabCompleter {

        private final Command command;

        public PaperCommand(Command command) {
            this.command = command;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            Sender commandSender = new PlatformImpl().getSender(sender);

            return this.command.checkAndExecute(commandSender, args);
        }

        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
            String[] newArgs = new String[args.length + 1];
            newArgs[0] = this.command.getName();
            System.arraycopy(args, 0, newArgs, 1, args.length);
            Sender commandSender = getPlatformImpl().getSender(sender);
            return this.command.tabComplete(commandSender, newArgs);
        }
    }
}