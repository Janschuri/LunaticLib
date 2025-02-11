package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LunaticHelpCommand extends AbstractLunaticCommand {

    private final LunaticLanguageConfig languageConfig;
    private final LunaticCommand command;
    private final String permission;
    private final int pageSize;

    private final MessageKey noPermission = new MessageKey("no_permission");
    private final MessageKey no_number = new MessageKey("no_number");

    public LunaticHelpCommand(LunaticLanguageConfig languageConfig, LunaticCommand command) {
        this.languageConfig = languageConfig;
        this.command = command;
        this.permission = command.getPermission();
        this.pageSize = 10;
    }

    public LunaticHelpCommand(LunaticLanguageConfig languageConfig, LunaticCommand command, String permission) {
        this.languageConfig = languageConfig;
        this.command = command;
        this.permission =  permission;
        this.pageSize = 10;
    }

    public LunaticHelpCommand(LunaticLanguageConfig languageConfig, LunaticCommand command, int pageSize) {
        this.languageConfig = languageConfig;
        this.command = command;
        this.permission = command.getPermission();
        this.pageSize = pageSize;
    }

    public LunaticHelpCommand(LunaticLanguageConfig languageConfig, LunaticCommand command, String permission, int pageSize) {
        this.languageConfig = languageConfig;
        this.command = command;
        this.permission =  permission;
        this.pageSize = pageSize;
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
    public String getName() {
        return "help";
    }

    @Override
    public LunaticCommand getParentCommand() {
        return command;
    }

    @Override
    public Map<String, String> getParam(int paramIndex) {
        int sizeSubcommands = command.getSubcommands().size();
        int pages = sizeSubcommands / pageSize;
        Map<String, String> map = new HashMap<>();
        if (pages >= 2) {
            for (int i = 1; i <= pages; i++) {
                map.put(String.valueOf(i), permission);
            }
        }
        return map;
    }

    @Override
    public boolean execute(Sender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(languageConfig.getPrefix().append(languageConfig.getMessage(noPermission)));
        } else {
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
        }
        return true;
    }

    private Component getHelpMessage(Sender sender, int page) {
        Component header = languageConfig.getHelpHeader(command.getName());

        ComponentBuilder builder = Component.text()
                .append(header);

        int start = (page - 1) * pageSize;

        List<Component> messages = new ArrayList<>();


        for (LunaticCommand subcommand : command.getSubcommands()) {

                if (subcommand.hasHelpCommand()) {
                    if (sender.hasPermission(subcommand.getHelpCommand().getPermission())) {
                        Component m = getMessage(new CommandMessageKey(subcommand.getHelpCommand(), "help").noPrefix());
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

    private Component getParamsHover(Sender sender, LunaticCommand subcommand, int paramsIndex) {
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

    private Component getAliasesHover(Sender sender, LunaticCommand subcommand) {
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

    private Component getReplacedComponent(Component message, Sender sender, LunaticCommand subcommand) {
        int paramsIndex = 0;

        while (message.toString().contains("%param%")) {
            TextReplacementConfig paramReplacement = TextReplacementConfig.builder()
                    .match("%param%")
                    .once()
                    .replacement(getParamsHover(sender, subcommand, paramsIndex))
                    .build();

            message = message.replaceText(paramReplacement);
            paramsIndex++;
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
