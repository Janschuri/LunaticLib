package de.janschuri.lunaticlib.common.config;

import de.janschuri.lunaticlib.LanguageConfig;

import java.nio.file.Path;
import java.util.*;

public abstract class AbstractLanguageConfig extends AbstractConfig implements LanguageConfig {
    private Map<String, String> messages = new HashMap<>();
    private String prefix;
    private String[] commands;
    private final Map<String, Map<String, List<String>>> aliases = new HashMap<>();

    public AbstractLanguageConfig(Path dataDirectory, String[] commands, String language) {
        super(dataDirectory, "lang.yml", "lang/" + language + ".yml");
        this.commands = commands;
    }

    public void load(){
        super.load();

        prefix = translateAlternateColorCodes('&', getString("prefix", "&8[&6LunaticFamily&8] "));

        messages = getStringMap("messages");



        for (String command : commands) {
            Map<String, List<String>> map = getStringListMap("aliases." + command);
            aliases.put(command, map);
        }
    }

    public String getMessage(String key) {

        if (messages.containsKey(key.toLowerCase())) {
            return translateAlternateColorCodes('&', messages.get(key));
        } else {
            return "Message '" + key.toLowerCase() + "' not found!";
        }
    }

    public List<String> getAliases(String command, String subcommand) {
        Map<String, List<String>> commandAliases = aliases.getOrDefault(command, new HashMap<>());

        List<String> subcommandsList = new ArrayList<>();

        List<String> list = commandAliases.getOrDefault(subcommand, new ArrayList<>());

        if (list.isEmpty()) {
            list.add(subcommand);
        }
        subcommandsList.addAll(list);

        return subcommandsList;
    }

    public List<String> getAliases(String command) {
        return getAliases(command, "base_command");
    }

    public boolean checkIsSubcommand(final String command, final String subcommand, final String arg) {
        return subcommand.equalsIgnoreCase(arg) || getAliases(command, subcommand).stream().anyMatch(element -> arg.equalsIgnoreCase(element));
    }

    public String getPrefix() {
        return prefix;
    }
}
