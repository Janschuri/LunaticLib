package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;
import de.janschuri.lunaticlib.common.config.LunaticMessageKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunaticHelpCommand extends LunaticCommand implements HasParentCommand, HasParams {

    private final LunaticLanguageConfig languageConfig;
    private final HasHelpCommand command;
    private String permission;
    private int pageSize;

    private final LunaticMessageKey noPermission = new LunaticMessageKey("no_permission");
    private final LunaticMessageKey no_number = new LunaticMessageKey("no_number");

    public LunaticHelpCommand(HasHelpCommand command) {
        this.languageConfig = command.getLanguageConfig();
        this.command = command;
        this.permission = command.getPermission();
        this.pageSize = 10;
    }

    public LunaticHelpCommand setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public LunaticHelpCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public LunaticLanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public List<Component> getParamsNames() {
        if (getPageAmount() < 2) {
            return List.of();
        }

        return List.of(
                getMessage(command.pageParamName())
        );
    }

    @Override
    public List<Map<String, String>> getParams() {
        if (getPageAmount() < 2) {
            return List.of();
        }

        return List.of(
                Map.of(getPageAmount()+"", permission)
        );
    }

    @Override
    public Component wrongUsageMessage(Sender sender, String[] args) {
        return command.wrongUsageMessage(sender, args);
    }

    @Override
    public Component noPermissionMessage(Sender sender, String[] args) {
        return command.noPermissionMessage(sender, args);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public HasHelpCommand getParentCommand() {
        return command;
    }

    @Override
    public Map<String, String> getParam(int paramIndex) {
        int pages = getPageAmount() / pageSize;
        Map<String, String> map = new HashMap<>();
        if (pages >= 2) {
            for (int i = 1; i <= pages; i++) {
                map.put(String.valueOf(i), permission);
            }
        }
        return map;
    }

    private int getPageAmount() {
        return command.getSubcommands().size() / pageSize;
    }

    @Override
    public boolean execute(Sender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getHelpMessage(sender, 1));
        } else {
            try {
                int page = Integer.parseInt(args[0]);
                sender.sendMessage(getHelpMessage(sender, page));
            } catch (NumberFormatException e) {
                sender.sendMessage(languageConfig.getPrefix().append(languageConfig.getMessage(no_number)));
            }
        }

        return true;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        return command.getHelpMessages();
    }

    private Component getHelpMessage(Sender sender, int page) {
        Component header = getMessage(command.getHelpHeader());

        ComponentBuilder builder = Component.text()
                .append(header);

        int start = (page - 1) * pageSize;

        List<Component> messages = new ArrayList<>();


        for (Command subcommand : command.getSubcommands()) {
                if (!subcommand.getHelpMessages().isEmpty()) {
                    for (Map.Entry<CommandMessageKey, String> entry : subcommand.getHelpMessages().entrySet()) {
                        if (sender.hasPermission(entry.getValue())) {
                            Component m = getReplacedHelpMessage(entry.getKey().noPrefix(), sender, command, subcommand);
                            messages.add(m);
                        }
                    }
                }
        }

        for (int i = start; i < start + pageSize; i++) {
            if (i < messages.size()) {
                builder.append(Component.newline());
                builder.append(messages.get(i));
            }
        }

        Component footer = languageConfig.getHelpFooter(command, page, (int) Math.ceil((double) messages.size() /pageSize));
        builder.append(Component.newline());
        builder.append(footer);


        return builder.build();
    }
}
