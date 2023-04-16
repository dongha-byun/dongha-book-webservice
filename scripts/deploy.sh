#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=dongha-book-webservice

echo "> Copy Build file!"

cp $REPOSITORY/zip/*.jar $REPOSITORY

echo "> Check PID of Current Running Application..."

CURRENT_PID=$(pgrep -fl dongha-book-webservice | grep jar | awk '{print $1}')

echo "> Current Running Application's PID ===> $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> Not Exists Running Application!"
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy New Application..."

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME : $JAR_NAME"
echo "> Add Execute Authority of $JAR_NAME"

chmod +x $JAR_NAME

echo "> Execute $JAR_NAME "

nohup java -jar \
      -Dspring.config.location=classpath:/application.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml,classpath:/application-real.yml \
      -Dspring.profiles.active=real \
      $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &