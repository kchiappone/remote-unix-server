package net.chiappone.unix.remote.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Helper class to close streams.
 *
 * @author Kurtis Chiappone
 */
public class StreamUtil {

    private static final Logger logger = LoggerFactory.getLogger( StreamUtil.class );

    /**
     * @param stream Closeable stream
     */
    public static void closeStream( Closeable stream ) {

        if ( stream != null ) {

            try {

                stream.close();

            } catch ( IOException e ) {

                logger.debug( "Error closing stream", e );

            }

        }
    }

}
