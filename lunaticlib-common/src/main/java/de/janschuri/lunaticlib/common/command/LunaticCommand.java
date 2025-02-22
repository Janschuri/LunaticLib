package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.config.HasMessageKeys;
import de.janschuri.lunaticlib.common.config.LunaticCommandMessageKey;
import de.janschuri.lunaticlib.common.config.LunaticLanguageConfig;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.*;
import java.util.stream.Collectors;

public abstract class LunaticCommand implements Command, HasMessageKeys {


    protected abstract LunaticLanguageConfig getLanguageConfig();

    @Override
    public List<String> tabComplete(Sender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission(getPermission())) {
            if (args.length == 0) {
                list.addAll(getAliases());
            } else if (args.length == 1) {
                for (String s : getAliases()) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(s);
                    }
                }
            } else {
                if (isAlias(args[0])) {
                    if (this instanceof HasSubcommands hasSubcommands) {
                        list.addAll(hasSubcommands.subcommandsTabComplete(sender, args));
                    }

                    if (this instanceof HasParams hasParams) {
                        list.addAll(hasParams.paramsTabComplete(sender, args));
                    }
                }
            }
        }

        return list;
    }

    @Override
    public String getPath() {
        if (this instanceof HasParentCommand hasParentCommand && !hasParentCommand.isPrimaryCommand()) {
            return "commands." + hasParentCommand.getParentCommand().getName() + ".subcommands." + this.getName();
        } else {
            return "commands." + this.getName();
        }
    }

    @Override
    public List<String> getDefaultAliases() {
        return List.of(getName());
    }

    @Override
    public Component getMessage(MessageKey key, Placeholder... placeholders) {
        return getLanguageConfig().getMessage(key, placeholders);
    }

    public List<String> getAliases() {
        List<String> list;

        if (this instanceof HasParentCommand hasParentCommand && !hasParentCommand.isPrimaryCommand()) {
            list = getLanguageConfig().getAliases(hasParentCommand.getParentCommand().getName(), getName());
        } else {
            list = getLanguageConfig().getAliases(getName());
        }

        if (list.isEmpty()) {
            list.add(getName());
        }

        return list;
    }

    public Component getPrefix() {
        return getLanguageConfig().getPrefix();
    }

    public boolean isAlias(String arg) {
        return getName().equalsIgnoreCase(arg) || getAliases().stream().anyMatch(element -> arg.equalsIgnoreCase(element));
    }

    public String getFullCommand() {
        if (this instanceof HasParentCommand hasParentCommand && !hasParentCommand.isPrimaryCommand()) {
            return hasParentCommand.getParentCommand().getFullCommand() + " " + getName();
        } else {
            return getName();
        }
    }

    public List<Component> getFormattedAliasesList(Sender sender) {
        List<Component> list = new ArrayList<>();

        for (String alias : getAliases()) {
                list.add(Component.text(alias));
        }
        return list;
    }

    protected LunaticPlaceholder placeholder(String key, String value) {
        return new LunaticPlaceholder(key, Component.text(value));
    }

    protected LunaticPlaceholder placeholder(String key, Component value) {
        return new LunaticPlaceholder(key, value);
    }

    @Override
    public boolean checkAndExecute(Sender commandSender, String[] args) {
        if (!checkPermission(commandSender, args)) {
            Logger.debugLog("Permission check failed");
            return true;
        }

        Logger.debugLog("Executing command " + getName() + " with args " + String.join(" ", args));
        return execute(commandSender, args);
    }

    @Override
    public boolean checkPermission(Sender commandSender, String[] args) {
        if (commandSender.hasPermission(getPermission())) {
            return true;
        } else {
            commandSender.sendMessage(noPermissionMessage(commandSender, args));
            return false;
        }
    }

    public Component getReplacedHelpMessage(MessageKey key, Sender sender, Command command, Command subcommand) {
        int paramsIndex = 0;

        Component message = getMessage(key);
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
}
