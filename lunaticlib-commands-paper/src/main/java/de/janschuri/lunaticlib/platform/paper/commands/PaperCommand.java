package de.janschuri.lunaticlib.platform.paper.commands;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.platform.paper.sender.PaperSenderHandler;
import de.janschuri.lunaticlib.sender.Sender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaperCommand implements CommandExecutor, TabCompleter {

    private final Command command;

    public PaperCommand(Command command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Sender commandSender = PaperSenderHandler.getAdapter().getSender(sender);
        return this.command.checkAndExecute(commandSender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = this.command.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        Sender commandSender = PaperSenderHandler.getAdapter().getSender(sender);
        return this.command.tabComplete(commandSender, newArgs);
    }
}