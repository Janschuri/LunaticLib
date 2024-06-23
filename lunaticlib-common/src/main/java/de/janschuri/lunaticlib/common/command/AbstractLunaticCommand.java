package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.*;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.common.logger.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.*;

public abstract class AbstractLunaticCommand implements LunaticCommand {

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
                    if (args[1].equalsIgnoreCase("")) {
                        if (!getParams().isEmpty() && args.length == 2) {
                            for (String s : getParam(0).keySet()) {
                                if (sender.hasPermission(getParam(0).get(s))) {
                                    list.add(s);
                                }
                            }
                        }
                        if (!getSubcommands().isEmpty()) {
                            String[] newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                            for (LunaticCommand lunaticCommand : getSubcommands()) {
                                list.addAll(lunaticCommand.tabComplete(sender, newArgs));
                            }
                        }
                    } else {
                        if (!getParams().isEmpty() && args.length == 2) {
                            for (String s : getParam(0).keySet()) {
                                if (sender.hasPermission(getParam(0).get(s))) {
                                    if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                        list.add(s);
                                    }
                                }
                            }
                        }
                        if (!getSubcommands().isEmpty()) {
                            String[] newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                            for (LunaticCommand lunaticCommand : getSubcommands()) {
                                list.addAll(lunaticCommand.tabComplete(sender, newArgs));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Map<CommandMessageKey, String> getHelpMessages() {
        Map<CommandMessageKey, String> map = new HashMap<>();

        if (hasHelpCommand()) {
            map.put(new CommandMessageKey(getHelpCommand(), "help"), getHelpCommand().getPermission());
        } else {
            map.put(new CommandMessageKey(this, "help"), getPermission());
        }

        return map;
    }

    @Override
    public Component getMessage(MessageKey key) {
        return getMessage(key, true);
    }

    @Override
    public Component getMessage(MessageKey key, boolean withPrefix) {
        if (isPrimaryCommand()) {
            return getLanguageConfig().getMessage(key, withPrefix);
        } else {
            return getLanguageConfig().getMessage(key, withPrefix);
        }
    }

    @Override
    public LunaticHelpCommand getHelpCommand() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        List<String> list = new ArrayList<>();
        if (isPrimaryCommand()) {
            list = getLanguageConfig().getAliases(getName());
        } else {
            list = getLanguageConfig().getAliases(getParentCommand().getName(), getName());
        }

        if (list.isEmpty()) {
            list.add(getName());
        }
        Logger.debugLog("Aliases: " + list.toString());
        return list;
    }

    @Override
    public Map<String, String> getParam(int paramIndex) {
        if (paramIndex < getParams().size()) {
            return getParams().get(paramIndex);
        } else {
            return new HashMap<>();
        }
    }

    @Override
    public Component getPrefix() {
        return getLanguageConfig().getPrefix();
    }

    @Override
    public LunaticCommand getParentCommand() {
        return null;
    }

    @Override
    public List<Map<String, String>> getParams() {
        return new ArrayList<>();
    }

    @Override
    public boolean hasParams() {
        return !getParams().isEmpty();
    }

    @Override
    public boolean isPrimaryCommand() {
        return getParentCommand() == null;
    }

    @Override
    public List<LunaticCommand> getSubcommands() {
        return new ArrayList<>();
    }

    @Override
    public boolean hasSubcommands() {
        return !getSubcommands().isEmpty();
    }

    @Override
    public boolean isAlias(String arg) {
        return getName().equalsIgnoreCase(arg) || getAliases().stream().anyMatch(element -> arg.equalsIgnoreCase(element));
    }

    @Override
    public String getFullCommand() {
        if (isPrimaryCommand()) {
            return getName();
        } else {
            return getParentCommand().getFullCommand() + " " + getName();
        }
    }

    @Override
    public boolean hasHelpCommand() {
        return getHelpCommand() != null;
    }

    @Override
    public Component getParamsName(int paramIndex) {
        if (getParamsNames().size() <= paramIndex) {
            return Component.text("params");
        }
        else {
            return getParamsNames().get(paramIndex);
        }
    }

    @Override
    public List<Component> getParamsNames() {
        return new ArrayList<>();
    }

    @Override
    public boolean isParam(int paramIndex, String arg) {
        return getParam(paramIndex).containsKey(arg);
    }

    @Override
    public boolean checkIsSubcommand(LunaticCommand subcommand, String arg) {
        return subcommand.isAlias(arg);
    }

    @Override
    public List<Component> getFormattedParamsList(Sender sender, int paramIndex) {
        List<Component> list = new ArrayList<>();
        for (String param : getParam(paramIndex).keySet()) {
            if (sender.hasPermission(getParam(paramIndex).get(param))) {
                list.add(Component.text(param));
            }
        }
        return list;
    }

    @Override
    public List<Component> getFormattedAliasesList(Sender sender) {
        List<Component> list = new ArrayList<>();

        for (String alias : getAliases()) {
                list.add(Component.text(alias));
        }
        return list;
    }

    protected TextReplacementConfig getTextReplacementConfig(String match, String replacement) {
        return TextReplacementConfig.builder()
                .match(match)
                .replacement(replacement)
                .build();
    }

    protected Map<String, String> getOnlinePlayersParam() {
        Collection<PlayerSender> players = LunaticLib.getPlatform().getOnlinePlayers();
        Map<String, String> playerParams = new HashMap<>();

        for (PlayerSender player : players) {
            playerParams.put(player.getName(), getPermission());
        }
        return playerParams;
    }


}
