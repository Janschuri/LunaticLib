package de.janschuri.lunaticlib.platform.bukkit.commands;

import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.LunaticCommand;
import de.janschuri.lunaticlib.platform.bukkit.PlatformImpl;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Command implements CommandExecutor, TabCompleter {

    private final LunaticCommand lunaticCommand;

    public Command(LunaticCommand lunaticCommand) {
        this.lunaticCommand = lunaticCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        Sender commandSender = new PlatformImpl().getSender(sender);

        return lunaticCommand.execute(commandSender, args);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = lunaticCommand.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        Sender commandSender = new PlatformImpl().getSender(sender);
        return lunaticCommand.tabComplete(commandSender, newArgs);
    }
}