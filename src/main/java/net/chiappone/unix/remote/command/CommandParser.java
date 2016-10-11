package net.chiappone.unix.remote.command;

import net.chiappone.unix.UnixCommander;
import net.chiappone.unix.remote.json.JsonRequest;

import java.net.Socket;

/**
 * @author Kurtis Chiappone
 */
public class CommandParser {

    /**
     * Parses the given request and returns a command. This allows us to create custom commands and use a catch-all
     * for typical Unix commands.
     *
     * @param commander UnixCommander
     * @param socket    Socket
     * @param request   JsonRequest
     * @return Command
     */
    public static Command parse( UnixCommander commander, Socket socket, JsonRequest request ) {

        if ( request == null || request.getCommand() == null || request.getCommand().trim().isEmpty() ) {

            return null;

        }

        String command = request.getCommand().trim();

        switch ( command.toLowerCase() ) {

            case "quit":
            case "exit":
            case "bye":
                return new TerminateCommand( socket );
            case "hello":
                return new HelloCommand();
            case "pid":
                return new ProcessIdCommand( commander );
            default:
                return new UnixCommand( commander, command );

        }

    }

}
