package net.chiappone.unix.remote.util;

import net.chiappone.util.file.FileModifiedMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Kurtis Chiappone
 */
public class ServerProperties {

    private static final Logger logger = LoggerFactory.getLogger( ServerProperties.class );
    private Properties properties = null;
    private Set<String> hostWhitelist = null;
    private int serverPort = 8221;
    private String secretKey = null;

    public ServerProperties() throws Exception {

        this.properties = new Properties();
        final String path = System.getProperty( "serverProperties", "server.properties" );
        final File propertiesFile = new File( path );

        if ( propertiesFile.exists() ) {

            loadProperties( path );
            TimerTask t = new FileModifiedMonitor( propertiesFile ) {

                @Override protected void onChange() {

                    try {

                        logger.info( "Properties file updated. Reloading..." );
                        loadProperties( path );

                    } catch ( Exception e ) {

                        logger.error( "Error reloading properties", e );

                    }

                }

            };

            Timer timer = new Timer();
            timer.scheduleAtFixedRate( t, 0, 60000 );

        }

    }

    public String getSecretKey() {

        return secretKey;
    }

    public void setSecretKey( String secretKey ) {

        this.secretKey = secretKey;

    }

    public int getServerPort() {

        return serverPort;
    }

    public void setServerPort( int serverPort ) {

        this.serverPort = serverPort;
    }

    public Properties getProperties() {

        return properties;
    }

    public Set<String> getHostWhitelist() {

        return hostWhitelist;
    }

    public void setHostWhitelist( Set<String> hostWhitelist ) {

        this.hostWhitelist = hostWhitelist;
    }

    private void loadProperties( String path ) throws IOException {

        properties.clear();
        properties.load( new FileReader( new File( path ) ) );
        setPropertyValues();

    }

    /**
     * Transforms the given property key into a Set.
     *
     * @param key property key
     * @return String Set of property values
     */
    private Set<String> keyToSet( String key ) {

        Set<String> set = new TreeSet<String>();
        String[] array = getAsArray( key );

        if ( array != null && array.length > 0 ) {

            for ( String s : array ) {

                set.add( s.trim() );

            }

        }

        return set;

    }

    /**
     * Initializes the property values.
     */
    private void setPropertyValues() {

        setHostWhitelist( keyToSet( "host.whitelist" ) );
        setServerPort( Integer.valueOf( getProperty( "server.port" ) ) );
        setSecretKey( getProperty( "secret.key" ) );

    }

    /**
     * Parses the given property as an array.
     *
     * @param key property key
     * @return String array
     */
    private String[] getAsArray( String key ) {

        String property = getProperty( key );

        if ( property != null && !property.trim().isEmpty() ) {

            return property.trim().split( "," );

        }

        return new String[] {};

    }

    /**
     * Delegate method to get a property value from key.
     *
     * @param key property key
     * @return String property value
     */
    public String getProperty( String key ) {

        return properties.getProperty( key );
    }

}
