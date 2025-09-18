package de.janschuri.lunaticlib.commands;

public interface HasParentCommand extends Command {

    Command getParentCommand();

    default boolean isPrimaryCommand() {
        return false;
    }
}
