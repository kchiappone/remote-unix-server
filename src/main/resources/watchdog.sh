#!/bin/bash

# Change <user_home> to your home directory. It is explicitly set so that cron can also run this.
REMOTE_UNIX_HOME=/<user_home>/remote-unix-server

if ps -ef | grep -v grep | grep $USER | grep "$REMOTE_UNIX_HOME/run\.sh" ; then
    exit 0
else
    nohup $REMOTE_UNIX_HOME/run.sh &
    exit 0
fi