#!/usr/bin/env bash

ABS_PATH=$(readlink -f "$0")
ABS_DIR=$(dirname "$ABS_PATH")
source "$ABS_DIR"/profile.sh
source "$ABS_DIR"/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT : $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:$IDLE_PORT/profile)
  UP_COUNT=$(echo "$RESPONSE" | grep 'real' | wc -l)

  if [ "$UP_COUNT" -ge 1 ]
  then
    echo "> Health check Success!"
    switch_proxy
    break
  else
    echo "> Health Check 의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
    echo "> Health Check: $RESPONSE"
  fi

  if [ "$RETRY_COUNT" -eq 10 ]
  then
    echo "> Health check Fail..."
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health Check 연결 실패. 재시도..."
  sleep 10
done