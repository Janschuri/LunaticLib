package de.janschuri.lunaticlib;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public interface LunaticCommand {

    List<String> tabComplete(Sender sender, String[] args);

    LunaticLanguageConfig getLanguageConfig();

    Map<CommandMessageKey, String> getHelpMessages();

    Component getMessage(MessageKey key, Placeholder... placeholders);

    Component getPrefix();

    String getPermission();

    String getName();

    LunaticCommand getParentCommand();

    boolean execute(Sender sender, String[] args);

    List<String> getAliases();
    List<Map<String, String>> getParams();

    Map<String, String> getParam(int paramIndex);
    LunaticCommand getHelpCommand();

    boolean hasParams();

    boolean isPrimaryCommand();

    List<LunaticCommand> getSubcommands();

    boolean hasSubcommands();

    boolean isAlias(String arg);

    String getFullCommand();

    boolean hasHelpCommand();

    Component getParamsName(int paramIndex);
    List<Component> getParamsNames();

    boolean isParam(int paramIndex, String arg);

    boolean checkIsSubcommand(LunaticCommand subcommand, String arg);

    List<Component> getFormattedParamsList(Sender sender, int paramIndex);

    List<Component> getFormattedAliasesList(Sender sender);

    default Placeholder placeholder(String key, String value) {
        return new Placeholder(key, Component.text(value));
    }

    default Placeholder placeholder(String key, Component value) {
        return new Placeholder(key, value);
    }
}
