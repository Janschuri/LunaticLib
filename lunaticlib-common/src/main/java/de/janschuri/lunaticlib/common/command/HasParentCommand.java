package de.janschuri.lunaticlib.common.command;

import de.janschuri.lunaticlib.Command;

public interface HasParentCommand extends Command {

    Command getParentCommand();

    default boolean isPrimaryCommand() {
        return false;
    }
}
