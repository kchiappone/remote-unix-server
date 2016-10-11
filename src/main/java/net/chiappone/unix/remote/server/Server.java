package net.chiappone.unix.remote.server;

import net.chiappone.unix.CommandResult;
import net.chiappone.unix.UnixCommander;
import net.chiappone.unix.remote.command.ProcessIdCommand;
import net.chiappone.unix.remote.util.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class for the app.
 *
 * @author Kurtis Chiappone
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger( Server.class );
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final UnixCommander commander = new UnixCommander();
    private static final int DEFAULT_PORT = 8221;
    private ServerProperties serverProperties = null;

    public static void main( String[] args ) {

        new Server().run();

    }

    public UnixCommander getCommander() {

        return commander;
    }

    public ServerProperties getServerProperties() {

        return serverProperties;
    }

    public void run() {

        initializeMdc();

        try {

            this.serverProperties = new ServerProperties();

        } catch ( Exception e ) {

            logger.error( "Error loading server.properties. Terminating." );
            System.exit( -1 );

        }

        // Initialize the server socket using the port from the properties file

        ServerSocket serverSocket = null;
        int port = getServerProperties().getServerPort();

        try {

            serverSocket = new ServerSocket();
            serverSocket.bind( new InetSocketAddress( port ) );

        } catch ( Exception e ) {

            logger.error( "Error starting server on port {}", port, e );
            System.exit( -1 );

        }

        // Spawn a RequestHandler thread for each client connection

        if ( serverSocket != null ) {

            logger.info( "Server listening on port {}", port );

            // This infinite loop is on purpose. The process has to be killed.
            //noinspection InfiniteLoopStatement
            while ( true ) {

                Socket clientSocket = null;

                try {

                    clientSocket = serverSocket.accept();

                } catch ( IOException e ) {

                    logger.warn( "Error handling client socket", e );

                }

                if ( clientSocket != null ) {

                    Thread t = new Thread( new RequestHandler( this, clientSocket ) );
                    t.setName( RequestHandler.class.getSimpleName() );
                    executorService.execute( t );

                }

            }

        }

    }

    /**
     * Adds the PID to MDC for logging purposes.
     */
    private void initializeMdc() {

        ProcessIdCommand command = new ProcessIdCommand( new UnixCommander() );
        CommandResult result = command.execute();

        if ( result != null && result.getOutput() != null ) {

            MDC.put( "pid", result.getOutput() );

        }

    }

}
