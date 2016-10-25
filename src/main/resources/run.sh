#!/bin/bash

# Change <user_home> to your home directory. It is explicitly set so that cron can also run this.
REMOTE_UNIX_HOME=/<user_home>/remote-unix-server
JAVA_HOME=$REMOTE_UNIX_HOME/jre

$JAVA_HOME/bin/java -Djava.net.preferIPv4Stack=true -DserverProperties=$REMOTE_UNIX_HOME/server.properties -Dlogback.configurationFile=$REMOTE_UNIX_HOME/logback.xml -jar $REMOTE_UNIX_HOME/remote-unix-server-1.1-jar-with-dependencies.jar