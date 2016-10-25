package net.chiappone.unix.remote.client;

import com.google.gson.Gson;
import net.chiappone.unix.CommandResult;
import net.chiappone.unix.remote.json.JsonRequest;
import net.chiappone.unix.remote.util.SecurityUtil;
import net.chiappone.unix.remote.util.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Kurtis Chiappone
 */
public class RemoteUnixClient {

    private static final Logger logger = LoggerFactory.getLogger( RemoteUnixClient.class );

    public static CommandResult execute( String secret, String host, int port, String command ) {

        return execute( secret, host, port, command, true, true );

    }

    public static CommandResult execute( String secret, String host, int port, String command, boolean logOutput,
                    boolean waitForProcess ) {

        CommandResult result = new CommandResult( -1, "" );
        PrintWriter out = null;
        BufferedReader in = null;
        Gson gson = new Gson();

        try {

            // Open a connection to the given host and port

            Socket socket = new Socket( host, port );

            if ( socket != null ) {

                String address = socket.getLocalAddress().getHostAddress();

                // Write the request to the server

                final String key = SecurityUtil.generateHash( secret, address );
                out = new PrintWriter( socket.getOutputStream(), true );
                JsonRequest request = new JsonRequest( key, command, logOutput, waitForProcess );
                out.println( gson.toJson( request ) );

                // Read the response from the server

                in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                String response = null;

                // Keep in mind that the response may span multiple lines

                while ( ( response = in.readLine() ) != null ) {

                    // Convert the server response from a JSON object to a CommandResult
                    // and print the output for the user

                    result = gson.fromJson( response, CommandResult.class );
                    break;

                }

            }

        } catch ( SocketException e ) {

            logger.debug( "Socket closed unexpectedly", e );

        } catch ( Exception e ) {

            logger.warn( "Error sending request", e );

        } finally {

            logger.debug( "Terminating {} thread", RemoteUnixClient.class.getSimpleName() );
            StreamUtil.closeStream( out );
            StreamUtil.closeStream( in );

        }

        return result;

    }

}
