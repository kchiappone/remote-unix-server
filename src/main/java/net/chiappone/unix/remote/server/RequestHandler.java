package net.chiappone.unix.remote.server;

import com.google.gson.Gson;
import net.chiappone.unix.CommandResult;
import net.chiappone.unix.remote.command.Command;
import net.chiappone.unix.remote.command.CommandParser;
import net.chiappone.unix.remote.json.JsonRequest;
import net.chiappone.unix.remote.util.SecurityUtil;
import net.chiappone.unix.remote.util.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;

/**
 * Handles each client connection.
 *
 * @author Kurtis Chiappone
 */
public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger( RequestHandler.class );
    private Server server = null;
    private Socket socket = null;

    public RequestHandler( Server server, Socket socket ) {

        this.server = server;
        this.socket = socket;

    }

    public Server getServer() {

        return server;
    }

    public Socket getSocket() {

        return socket;
    }

    @Override public void run() {

        // Get the client IP address and add it to the MDC for logging purposes

        String clientAddress = getSocket().getInetAddress().getHostAddress();
        MDC.put( "caddress", clientAddress );
        logger.info( "{} connected", clientAddress );

        BufferedReader in = null;
        PrintWriter out = null;
        Gson gson = new Gson();

        try {

            in = new BufferedReader( new InputStreamReader( getSocket().getInputStream() ) );
            out = new PrintWriter( getSocket().getOutputStream(), true );

            // Verify that the host is trusted

            Set<String> hostWhitelist = getServer().getServerProperties().getHostWhitelist();

            if ( hostWhitelist == null || hostWhitelist.isEmpty() ) {

                logger.error( "Host whitelist is empty. No hosts will be able to connect." );

            }

            if ( hostWhitelist.contains( clientAddress ) ) {

                String request;

                // Keep looping as long as the connection is live and we have a request to read

                while ( getSocket().isConnected() && ( request = in.readLine() ) != null ) {

                    // Get the request and verify the security key

                    JsonRequest jsonRequest = gson.fromJson( request, JsonRequest.class );
                    logger.info( "{}", jsonRequest.getCommand() );

                    String secretKey = getServer().getServerProperties().getSecretKey();

                    if ( secretKey == null || secretKey.trim().isEmpty() ) {

                        logger.warn( "Secret Key is blank. This may pose a security risk!" );

                    }

                    boolean verified = SecurityUtil.verifyKey( secretKey, clientAddress, jsonRequest.getKey() );

                    // If the request is verified, parse and process the command

                    if ( verified ) {

                        Command command = CommandParser.parse( getServer().getCommander(), getSocket(), jsonRequest );
                        CommandResult result = new CommandResult( -1, "Unsupported" );

                        if ( command != null ) {

                            result = command.execute();

                        }

                        out.println( gson.toJson( result ) );

                    }

                    // The request could not be verified, so reject it.

                    else {

                        reject( out, gson );

                    }

                }

            }

            // The host could not be verified, so reject the connection.

            else {

                reject( out, gson );

            }

        } catch ( SocketException e ) {

            logger.debug( "Socket closed unexpectedly", e );

        } catch ( Exception e ) {

            logger.warn( "Error handling client request", e );

        } finally {

            logger.info( "Terminating client connection: {}", clientAddress );
            StreamUtil.closeStream( out );
            StreamUtil.closeStream( in );

        }

    }

    private void reject( PrintWriter out, Gson gson ) {

        CommandResult result = new CommandResult( -1, "Authentication required" );
        out.println( gson.toJson( result ) );
    }

}
