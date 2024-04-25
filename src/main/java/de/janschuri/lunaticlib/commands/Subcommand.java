package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.config.Language;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.utils.ClickableMessage;
import de.janschuri.lunaticlib.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Subcommand {
    protected final String permission;
    protected final List<String> aliases;
    protected final String name;
    protected final String mainCommand;
    protected List<String> params;
    protected Subcommand[] subcommands;
    protected final Language language;


    protected Subcommand(Language language, String mainCommand, String name, String permission) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.language = language;
        this.aliases = language.getAliases(mainCommand, name);
    }

    protected Subcommand(Language language, String mainCommand, String name, String permission, List<String> params) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.language = language;
        this.aliases = language.getAliases(mainCommand, name);
        this.params = params;
    }

    protected Subcommand(Language language, String mainCommand, String name, String permission, Subcommand[] subcommands) {
        this.mainCommand = mainCommand;
        this.name = name;
        this.permission = permission;
        this.language = language;
        this.aliases = language.getAliases(mainCommand, name);
        this.subcommands = subcommands;
    }

    public List<String> tabComplete(AbstractSender sender, String[] args) {
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
                if (language.checkIsSubcommand(mainCommand, name, args[0])) {
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
        System.out.println(Arrays.toString(args) + " || " + list);
        return list;
    }

    public ClickableMessage getHelp(AbstractSender sender) {
        if (sender.hasPermission(permission)) {
            if (subcommands != null) {
                return new ClickableMessage(language.getMessage(mainCommand + "_" + name + "_help") + "\n", language.getMessage(name + "_help"), mainCommand + " " + name + " help");
            } else {
                return new ClickableMessage(language.getMessage(mainCommand + "_" + name + "_help") + "\n");
            }
        } else {
          return ClickableMessage.empty();
        }
    }

    public abstract boolean execute(AbstractSender sender, String[] args);

}
