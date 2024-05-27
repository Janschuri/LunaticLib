package de.janschuri.lunaticlib.common.commands;

import de.janschuri.lunaticlib.LanguageConfig;
import de.janschuri.lunaticlib.Sender;
import de.janschuri.lunaticlib.Subcommand;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSubcommand implements Subcommand {

    protected final String permission;
    protected final List<String> aliases;
    protected final String name;
    protected final String mainCommand;
    protected List<String> params;
    protected AbstractSubcommand[] subcommands;
    protected final LanguageConfig languageConfig;


    protected AbstractSubcommand(LanguageConfig languageConfig, String mainCommand, String name, String permission) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.languageConfig = languageConfig;
        this.aliases = languageConfig.getAliases(mainCommand, name);
    }

    protected AbstractSubcommand(LanguageConfig languageConfig, String mainCommand, String name, String permission, List<String> params) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.languageConfig = languageConfig;
        this.aliases = languageConfig.getAliases(mainCommand, name);
        this.params = params;
    }

    protected AbstractSubcommand(LanguageConfig languageConfig, String mainCommand, String name, String permission, AbstractSubcommand[] subcommands) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.languageConfig = languageConfig;
        this.aliases = languageConfig.getAliases(mainCommand, name);
        this.subcommands = subcommands;
    }

    public List<String> tabComplete(Sender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission(permission)) {
            if (args.length == 0) {
                list.addAll(aliases);
            } else if (args.length == 1) {
                for (String s : aliases) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(s);
                    }
                }
            } else {
                if (languageConfig.checkIsSubcommand(mainCommand, name, args[0])) {
                    if (args[1].equalsIgnoreCase("")) {
                        if (params != null && args.length == 2) {
                            list.addAll(params);
                        }
                        if (subcommands != null) {
                            String[] newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                            for (Subcommand subcommand : subcommands) {
                                list.addAll(subcommand.tabComplete(sender, newArgs));
                            }
                        }
                    } else {
                        if (params != null && args.length == 2) {
                            for (String s : params) {
                                if (s.toLowerCase().startsWith(args[1].toLowerCase())) {
                                    list.add(s);
                                }
                            }
                        }
                        if (subcommands != null) {
                            String[] newArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                            for (Subcommand subcommand : subcommands) {
                                list.addAll(subcommand.tabComplete(sender, newArgs));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public Component getHelp(Sender sender) {
//        if (sender.hasPermission(permission)) {
//            if (subcommands != null) {
//                return new ClickableMessage(language.getMessage(mainCommand + "_" + name + "_help") + "\n", language.getMessage(name + "_help"), "/" + mainCommand + " " + name + " help");
//            } else {
//                return new ClickableMessage(language.getMessage(mainCommand + "_" + name + "_help") + "\n");
//            }
//        } else {
//          return ClickableMessage.empty();
//        }
        return Component.text("Help");
    }

    public String getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }

    public abstract boolean execute(Sender sender, String[] args);

    public List<String> getAliases() {
        return aliases;
    }
}
