package de.janschuri.lunaticlib;

import java.util.List;

public interface LanguageConfig extends Config {

    public String getMessage(String key);

    public List<String> getAliases(String command, String subcommand);
    public List<String> getAliases(String command);
    public boolean checkIsSubcommand(final String command, final String subcommand, final String arg);
    public String getPrefix();
}
