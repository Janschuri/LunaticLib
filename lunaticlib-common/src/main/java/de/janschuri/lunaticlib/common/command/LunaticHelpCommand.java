package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;
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

    public LunaticHelpCommand(LunaticLanguageConfig languageConfig, HasHelpCommand command) {
        this.languageConfig = languageConfig;
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
                command.pageParamName()
        );
    }

    @Override
    public List<Map<String, String>> getParams() {
        if (getPageAmount() < 2) {
            return List.of();
        }

        return List.of(
                Map.of("page", permission)
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

    private Component getHelpMessage(Sender sender, int page) {
        Component header = languageConfig.getHelpHeader(command.getName());

        ComponentBuilder builder = Component.text()
                .append(header);

        int start = (page - 1) * pageSize;

        List<Component> messages = new ArrayList<>();


        for (Command subcommand : command.getSubcommands()) {

                if (subcommand instanceof HasHelpCommand hasHelpCommand) {
                    if (sender.hasPermission(hasHelpCommand.getHelpCommand().getPermission())) {
                        Component m = getMessage(new LunaticCommandMessageKey(hasHelpCommand.getHelpCommand(), "help").noPrefix());
                        m = getReplacedComponent(m, sender, subcommand);
                        messages.add(m);
                    }
                } else if (!subcommand.getHelpMessages().isEmpty()) {
                    for (Map.Entry<CommandMessageKey, String> entry : subcommand.getHelpMessages().entrySet()) {
                        if (sender.hasPermission(entry.getValue())) {
                            Component m = subcommand.getMessage(entry.getKey().noPrefix());
                            m = getReplacedComponent(m, sender, subcommand);
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

        Component footer = languageConfig.getHelpFooter(command.getName(), page, (int) Math.ceil((double) messages.size() /pageSize));
        builder.append(Component.newline());
        builder.append(footer);


        return builder.build();
    }

    private Component getParamsHover(Sender sender, HasParams subcommand, int paramsIndex) {
        List<Component> params = subcommand.getFormattedParamsList(sender, paramsIndex);
        Component paramsName = subcommand.getParamsName(paramsIndex);

        ComponentBuilder paramsHover = Component.text();

        int index = 0;
        for (Component param : params) {
            paramsHover.append(param);
            if (index < params.size() - 1) {
                paramsHover.append(Component.newline());
            }
        }

        return paramsName.hoverEvent(HoverEvent.showText(paramsHover.build()));
    }

    private Component getAliasesHover(Sender sender, Command subcommand) {
        List<Component> aliases = subcommand.getFormattedAliasesList(sender);

        ComponentBuilder aliasesHover = Component.text();

        int index = 0;
        for (Component alias : aliases) {
            aliasesHover.append(alias);
            if (index < aliases.size() - 1) {
                aliasesHover.append(Component.newline());
            }
        }

        return aliases.get(0).hoverEvent(HoverEvent.showText(aliasesHover.build()));
    }

    private Component getReplacedComponent(Component message, Sender sender, Command subcommand) {
        int paramsIndex = 0;

        if (subcommand instanceof HasParams hasParams) {
            while (message.toString().contains("%param%")) {
                TextReplacementConfig paramReplacement = TextReplacementConfig.builder()
                        .match("%param%")
                        .once()
                        .replacement(getParamsHover(sender, hasParams, paramsIndex))
                        .build();

                message = message.replaceText(paramReplacement);
                paramsIndex++;
            }
        }

        TextReplacementConfig commandReplacement = TextReplacementConfig.builder()
                .match("%command%")
                .replacement(getAliasesHover(sender, command))
                .build();

        TextReplacementConfig subcommandReplacement = TextReplacementConfig.builder()
                .match("%subcommand%")
                .replacement(getAliasesHover(sender, subcommand))
                .build();

        message = message.replaceText(commandReplacement);
        message = message.replaceText(subcommandReplacement);

        String commandString = "/" + subcommand.getFullCommand();

        message = message.clickEvent(ClickEvent.suggestCommand(commandString));

        return message;
    }
}
