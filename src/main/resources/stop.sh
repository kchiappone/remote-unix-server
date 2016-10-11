#!/bin/bash

# Change <user_home> to your home directory. It is explicitly set so that cron can also run this.
REMOTE_UNIX_HOME=/<user_home>/remote-unix-server

ps aux | grep -v grep | grep $USER | grep "$REMOTE_UNIX_HOME/run.sh" | awk {'print $2'} | xargs -r kill -9
ps aux | grep -v grep | grep $USER | grep "$REMOTE_UNIX_HOME" | grep jar | awk {'print $2'} | xargs -r kill -9