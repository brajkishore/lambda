#!/bin/sh
APP_NAME=lambda-0.0.1.jar
PORT=8789
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
if [ -z "$pid" ]
then
      echo "$APP_NAME not running,starting"
      nohup java -jar -Dserver.port=$PORT -Dspring.profiles.active=prod $APP_NAME &
else
      echo "$APP_NAME is already running with pid $pid"

fi
