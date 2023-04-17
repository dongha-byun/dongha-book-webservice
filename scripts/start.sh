#!/usr/bin/env bash

ABS_PATH=$(readlink -f "$0")
ABS_DIR=$(dirname "$ABS_PATH")
source "$ABS_DIR"/profile.sh

REPOSITORY=/home/ec2-user/app/step3

echo "> Copy Build file!"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Deploy New Application..."
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME : $JAR_NAME"
echo "> Add Execute Authority of $JAR_NAME"
chmod +x "$JAR_NAME"

echo "> Execute $JAR_NAME "
IDLE_PROFILE=$(find_idle_profile)
echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."
nohup java -jar \
      -Dspring.config.location=classpath:/application.yml,classpath:/application-"$IDLE_PROFILE".yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-real-db.yml \
      -Dspring.profiles.active="$IDLE_PROFILE" \
      "$JAR_NAME" > $REPOSITORY/nohup.out 2>&1 &
