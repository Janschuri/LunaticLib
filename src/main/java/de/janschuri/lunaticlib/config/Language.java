package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.utils.Logger;

import java.nio.file.Path;
import java.util.*;

public abstract class Language extends Config {
    private static Map<String, String> messages = new HashMap<>();
    public static String prefix;
    private static final List<String> commands = new ArrayList<>();
    private static final Map<String, Map<String, List<String>>> aliases = new HashMap<>();

    public Language(Path dataDirectory, List<String> commands, String language) {
        super(dataDirectory, "lang.yml", "lang/" + language + ".yml");
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

    public static String getMessage(String key) {

        if (messages.containsKey(key.toLowerCase())) {
            Logger.debugLog(translateAlternateColorCodes('&', messages.get(key)));
            return translateAlternateColorCodes('&', messages.get(key));
        } else {
            return "Message '" + key.toLowerCase() + "' not found!";
        }
    }

    public static List<String> getAliases(String command, String subcommand) {
        Map<String, List<String>> commandAliases = aliases.getOrDefault(command, new HashMap<>());

        List<String> subcommandsList = new ArrayList<>();

        List<String> list = commandAliases.getOrDefault(subcommand, new ArrayList<>());

        if (list.isEmpty()) {
            list.add(subcommand);
        }
        subcommandsList.addAll(list);

        return subcommandsList;
    }

    public static List<String> getAliases(String command) {
        return getAliases(command, "basecommand");
    }
}
