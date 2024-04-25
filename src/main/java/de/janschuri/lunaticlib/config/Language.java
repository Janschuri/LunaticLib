package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.utils.Logger;

import java.nio.file.Path;
import java.util.*;

public abstract class Language extends Config {
    private static Language instance;
    private static Map<String, String> messages = new HashMap<>();
    private static String prefix;
    private static final List<String> commands = new ArrayList<>();
    private static final Map<String, Map<String, List<String>>> aliases = new HashMap<>();

    public Language(Path dataDirectory, List<String> commands, String language) {
        super(dataDirectory, "lang.yml", "lang/" + language + ".yml");
        Language.instance = this;
        this.load();
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

    public static Language getInstance() {
        return instance;
    }

    public String getMessage(String key) {

        if (messages.containsKey(key.toLowerCase())) {
            Logger.debugLog(translateAlternateColorCodes('&', messages.get(key)));
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
        return getAliases(command, "basecommand");
    }

    public boolean checkIsSubcommand(final String command, final String subcommand, final String arg) {
        return subcommand.equalsIgnoreCase(arg) || getAliases(command, subcommand).stream().anyMatch(element -> arg.equalsIgnoreCase(element));
    }

    public String getPrefix() {
        return prefix;
    }
}
