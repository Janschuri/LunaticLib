package de.janschuri.lunaticlib.commands.platform;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.LunaticCommands;
import de.janschuri.lunaticlib.commands.platform.impl.PlatformImpl;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class VelocityLunaticLibCommands extends LunaticCommands {

    private static PlatformImpl platform;
    private static ProxyServer proxy;
    private static Object instance;

    public static void enable(ProxyServer proxy, Object instance) {
        if (platform == null) {
            platform = new PlatformImpl();
            setPlatform(platform);
        }

        VelocityLunaticLibCommands.proxy = proxy;
        VelocityLunaticLibCommands.instance = instance;
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Object getInstance() {
        return instance;
    }

    public static class VelocityCommand implements SimpleCommand {

        private final Command lunaticCommand;

        public VelocityCommand(Command lunaticCommand) {
            this.lunaticCommand = lunaticCommand;
        }


        @Override
        public void execute(final Invocation invocation) {
            CommandSource sender = invocation.source();
            String[] args = invocation.arguments();

            getProxy().getScheduler().buildTask(getInstance(), () -> {
                Sender commandSender = new PlatformImpl().getSender(sender);
                if (!lunaticCommand.checkAndExecute(commandSender, args)) {
                    Component errorMessage = Component.text("Internal server error. Please check the console for more information.")
                            .color(TextColor.fromHexString("#FF0000"));
                    commandSender.sendMessage(errorMessage);
                }
            }).schedule();
        }

        @Override
        public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
            CommandSource source = invocation.source();
            String[] args = invocation.arguments();
            int newSize = args.length > 0 ? args.length + 1 : 2;
            String[] newArgs = new String[newSize];
            newArgs[0] = lunaticCommand.getName();
            if (args.length == 0) {
                newArgs[1] = "";
            }
            System.arraycopy(args, 0, newArgs, 1, args.length);
            return CompletableFuture.completedFuture(lunaticCommand.tabComplete(new PlatformImpl().getSender(source), newArgs));
        }
    }
}