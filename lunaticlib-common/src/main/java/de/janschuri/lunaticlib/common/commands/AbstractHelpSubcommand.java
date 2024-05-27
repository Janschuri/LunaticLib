package de.janschuri.lunaticlib.common.commands;

import de.janschuri.lunaticlib.LanguageConfig;
import de.janschuri.lunaticlib.Subcommand;
import de.janschuri.lunaticlib.PlayerSender;
import de.janschuri.lunaticlib.Sender;
import net.kyori.adventure.text.Component;

public class AbstractHelpSubcommand extends AbstractSubcommand {

    private final LanguageConfig languageConfig;

    public AbstractHelpSubcommand(LanguageConfig languageConfig, String mainCommand, String name, String permission, AbstractSubcommand[] subcommands) {
        super(languageConfig, mainCommand, name, permission);
        this.languageConfig = languageConfig;
        this.subcommands = subcommands;
    }

    @Override
    public boolean execute(Sender sender, String[] args) {
        if (!(sender instanceof PlayerSender)) {
            sender.sendMessage(languageConfig.getPrefix() + languageConfig.getMessage("no_console_command"));
            return true;
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(languageConfig.getPrefix() + languageConfig.getMessage("no_permission"));
            return true;
        }

        PlayerSender playerSender = (PlayerSender) sender;

        Component msg = Component.text(languageConfig.getPrefix() + languageConfig.getMessage(mainCommand + "_help") + "\n");

            for (Subcommand subcommand : subcommands) {
                if (!(subcommand instanceof AbstractHelpSubcommand)) {
                    msg.append(subcommand.getHelp(sender));
                }
            }

        return true;
    }
}
