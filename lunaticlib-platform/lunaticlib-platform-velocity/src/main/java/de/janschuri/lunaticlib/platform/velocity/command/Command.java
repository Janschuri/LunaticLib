package de.janschuri.lunaticlib.platform.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.Subcommand;
import de.janschuri.lunaticlib.platform.velocity.PlatformImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Command implements SimpleCommand {

    private final Subcommand subcommand;

    public Command(Subcommand subcommand) {
        this.subcommand = subcommand;
    }


    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        Sender commandSender = new PlatformImpl().getSender(source);
        subcommand.execute(commandSender, args);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(subcommand.getPermission());
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        int newSize = args.length > 0 ? args.length + 1 : 2;
        String[] newArgs = new String[newSize];
        newArgs[0] = subcommand.getName();
        if (args.length == 0) {
            newArgs[1] = "";
        }
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return CompletableFuture.completedFuture(subcommand.tabComplete(new PlatformImpl().getSender(source), newArgs));
    }
}