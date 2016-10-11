package net.chiappone.unix.remote.command;

import net.chiappone.unix.CommandResult;
import net.chiappone.unix.UnixCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Catch-all Unix command. These will be executed using bash.
 *
 * @author Kurtis Chiappone
 */
public class UnixCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger( UnixCommand.class );
    private String command = null;
    private UnixCommander commander = null;

    public UnixCommand( UnixCommander commander, String command ) {

        this.commander = commander;
        this.command = command;

    }

    @Override public CommandResult execute() {

        logger.info( "{}", command );

        List<String> commands = new ArrayList<String>();
        commands.add( "/bin/bash" );
        commands.add( "-c" );
        commands.add( command );
        return commander.execute( commands );

    }

}
