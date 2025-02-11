package de.janschuri.lunaticlib;

public class CommandMessageKey extends MessageKey {

    private final LunaticCommand command;
    private final String key;

    public CommandMessageKey(LunaticCommand command, String key) {
        super(key);
        this.command = command;
        this.key = key;
    }

    @Override
    public String toString() {
        if (command.isPrimaryCommand()) {
            return "commands." + command.getName() + ".messages." + key;
        } else {
            return "commands." + command.getParentCommand().getName() + ".subcommands." + command.getName() + ".messages." + key;
        }
    }
}
