package net.chiappone.unix.remote.command;

import net.chiappone.unix.CommandResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Command to drop the client connection.
 *
 * @author Kurtis Chiappone
 */
public class TerminateCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger( TerminateCommand.class );
    private Socket socket = null;

    public TerminateCommand( Socket socket ) {

        this.socket = socket;

    }

    @Override public CommandResult execute() {

        CommandResult result = new CommandResult();

        try {

            socket.shutdownOutput();
            result.setOutput( "bye" );

        } catch ( Exception e ) {

            logger.error( "Error shutting down socket", e );

        }

        return result;

    }
}
