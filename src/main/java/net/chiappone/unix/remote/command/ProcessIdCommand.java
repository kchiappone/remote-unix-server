package net.chiappone.unix.remote.command;

import net.chiappone.unix.CommandResult;
import net.chiappone.unix.UnixCommander;

import java.util.ArrayList;
import java.util.List;

/**
 * Returns the PID of the process.
 *
 * @author Kurtis Chiappone
 */
public class ProcessIdCommand implements Command {

    private UnixCommander commander = null;

    public ProcessIdCommand( UnixCommander commander ) {

        this.commander = commander;

    }

    @Override public CommandResult execute() {

        List<String> command = new ArrayList<String>();
        command.add( "/bin/bash" );
        command.add( "-c" );
        command.add( "cut -d ' ' -f 4 /proc/self/stat" );
        return commander.execute( command );

    }
}
