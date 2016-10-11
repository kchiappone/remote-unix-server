#!/bin/bash

# Change <user_home> to your home directory. It is explicitly set so that cron can also run this.
REMOTE_UNIX_HOME=/<user_home>/remote-unix-server

$REMOTE_UNIX_HOME/stop.sh
nohup $REMOTE_UNIX_HOME/run.sh &