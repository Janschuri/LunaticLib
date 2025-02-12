package de.janschuri.lunaticlib.platform.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import de.janschuri.lunaticlib.Command;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.platform.velocity.PlatformImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VelocityCommand implements SimpleCommand {

    private final Command command;

    public VelocityCommand(Command command) {
        this.command = command;
    }


    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        Sender commandSender = new PlatformImpl().getSender(source);
        command.checkAndExecute(commandSender, args);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        int newSize = args.length > 0 ? args.length + 1 : 2;
        String[] newArgs = new String[newSize];
        newArgs[0] = command.getName();
        if (args.length == 0) {
            newArgs[1] = "";
        }
        System.arraycopy(args, 0, newArgs, 1, args.length);
        return CompletableFuture.completedFuture(command.tabComplete(new PlatformImpl().getSender(source), newArgs));
    }
}