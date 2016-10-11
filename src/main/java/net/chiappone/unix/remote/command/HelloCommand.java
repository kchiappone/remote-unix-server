package net.chiappone.unix.remote.command;

import net.chiappone.unix.CommandResult;

/**
 * Simply echoes "hello" back to the client.
 *
 * @author Kurtis Chiappone
 */
public class HelloCommand implements Command {

    @Override public CommandResult execute() {

        return new CommandResult( 0, "hello" );

    }
}
