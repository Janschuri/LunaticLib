package de.janschuri.lunaticlib.commands.impl;

import de.janschuri.lunaticlib.commands.Command;

public interface HasParentCommand extends Command {

    Command getParentCommand();

    default boolean isPrimaryCommand() {
        return false;
    }
}
