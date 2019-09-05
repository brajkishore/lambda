#!/bin/sh
APP_NAME=lambda-0.0.1.jar
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
echo $pid
if [ -z "$pid" ]
then
      echo "$APP_NAME not running"      
else
      echo "$APP_NAME running with pid $pid"
      echo "Stopping pid $pid"
      kill -9 $pid
      echo "Killed pid $pid"
fi
