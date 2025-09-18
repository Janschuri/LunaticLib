package de.janschuri.lunaticlib.platform.velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.platform.velocity.sender.VelocitySenderHandler;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class VelocityCommandHandler {

    private static VelocityCommandAdapter adapter;

    public static void enable(Object instance, ProxyServer proxy) {
        if (adapter == null) {
            synchronized (VelocityCommandHandler.class) {
                if (adapter == null) {
                    adapter = new VelocityCommandAdapter(instance, proxy);
                }
            }
        }
    }

    public static VelocityCommandAdapter adapter() {
        if (adapter == null) {
            throw new IllegalStateException("VelocityCommandAdapter is not initialized. Please call VelocityCommandHandler.enable() first.");
        }
        return adapter;
    }

    public static class VelocityCommand implements SimpleCommand {

        private final ProxyServer proxyServer;
        private final Object pluginInstance;
        private final Command lunaticCommand;

        public VelocityCommand(ProxyServer proxyServer, Object pluginInstance, Command lunaticCommand) {
            this.proxyServer = proxyServer;
            this.pluginInstance = pluginInstance;
            this.lunaticCommand = lunaticCommand;
        }


        @Override
        public void execute(final Invocation invocation) {
            CommandSource sender = invocation.source();
            String[] args = invocation.arguments();

            proxyServer.getScheduler().buildTask(pluginInstance, () -> {
                Sender commandSender = VelocitySenderHandler.adapter().getSender(sender);
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
            return CompletableFuture.completedFuture(lunaticCommand.tabComplete(VelocitySenderHandler.adapter().getSender(source), newArgs));
        }
    }
}