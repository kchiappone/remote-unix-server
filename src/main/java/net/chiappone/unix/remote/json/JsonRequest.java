package net.chiappone.unix.remote.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Kurtis Chiappone
 */
public class JsonRequest {

    private static transient final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {

        @Override protected DateFormat initialValue() {

            return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );

        }
    };
    private String timestamp = null;
    private String key = null;
    private String command = null;
    private boolean waitForProcess = true;
    private boolean logOutput = true;

    public JsonRequest( String key, String command ) {

        this.key = key;
        this.command = command;
        this.timestamp = df.get().format( new Date() );

    }

    public JsonRequest( String key, String command, boolean logOutput, boolean waitForProcess ) {

        this.key = key;
        this.command = command;
        this.timestamp = df.get().format( new Date() );
        this.logOutput = logOutput;
        this.waitForProcess = waitForProcess;

    }

    public boolean isLogOutput() {

        return logOutput;
    }

    public void setLogOutput( boolean logOutput ) {

        this.logOutput = logOutput;
    }

    @Override public boolean equals( Object o ) {

        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        JsonRequest that = (JsonRequest) o;

        if ( waitForProcess != that.waitForProcess )
            return false;
        if ( logOutput != that.logOutput )
            return false;
        if ( timestamp != null ? !timestamp.equals( that.timestamp ) : that.timestamp != null )
            return false;
        if ( key != null ? !key.equals( that.key ) : that.key != null )
            return false;
        return command != null ? command.equals( that.command ) : that.command == null;

    }

    @Override public int hashCode() {

        int result = timestamp != null ? timestamp.hashCode() : 0;
        result = 31 * result + ( key != null ? key.hashCode() : 0 );
        result = 31 * result + ( command != null ? command.hashCode() : 0 );
        result = 31 * result + ( waitForProcess ? 1 : 0 );
        result = 31 * result + ( logOutput ? 1 : 0 );
        return result;
    }

    public boolean isWaitForProcess() {

        return waitForProcess;
    }

    public void setWaitForProcess( boolean waitForProcess ) {

        this.waitForProcess = waitForProcess;
    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp( String timestamp ) {

        this.timestamp = timestamp;
    }

    @Override public String toString() {

        return command;
    }

    public String getKey() {

        return key;
    }

    public void setKey( String key ) {

        this.key = key;
    }

    public String getCommand() {

        return command;
    }

    public void setCommand( String command ) {

        this.command = command;
    }
}
