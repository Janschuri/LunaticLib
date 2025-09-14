package de.janschuri.lunaticlib.commands.platform.waterfall;

import de.janschuri.lunaticlib.commands.Command;
import de.janschuri.lunaticlib.commands.LunaticLibCommands;
import de.janschuri.lunaticlib.commands.platform.waterfall.impl.PlatformImpl;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public final class WaterfallLunaticLibCommands extends LunaticLibCommands {

    private static PlatformImpl platform;
    private static Plugin plugin;

    public static void enable(Plugin instance) {
        if (platform == null) {
            platform = new PlatformImpl();
            setPlatform(platform);
        }

        WaterfallLunaticLibCommands.plugin = instance;
    }

    public static Plugin getInstance() {
        return WaterfallLunaticLibCommands.plugin;
    }

    public static class WaterfallCommand extends net.md_5.bungee.api.plugin.Command implements TabExecutor {

        private final Command lunaticCommand;

        public WaterfallCommand(Command lunaticCommand) {
            super(lunaticCommand.getName(), lunaticCommand.getPermission(), lunaticCommand.getAliases().toArray(new String[0]));
            this.lunaticCommand = lunaticCommand;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {

            ProxyServer.getInstance().getScheduler().runAsync(WaterfallLunaticLibCommands.getInstance(), () -> {
                Sender commandSender = new PlatformImpl().getSender(sender);
                if (!lunaticCommand.checkAndExecute(commandSender, args)) {
                    Component errorMessage = Component.text("Internal server error. Please check the console for more information.")
                            .color(TextColor.fromHexString("#FF0000"));
                    commandSender.sendMessage(errorMessage);
                }
            });
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            String[] newArgs = new String[args.length + 1];
            newArgs[0] = lunaticCommand.getName();
            System.arraycopy(args, 0, newArgs, 1, args.length);
            Sender commandSender = new PlatformImpl().getSender(sender);
            return lunaticCommand.tabComplete(commandSender, newArgs);
        }
    }
}