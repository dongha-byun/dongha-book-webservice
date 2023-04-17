#!/usr/bin/env bash

#실행 중이지 않은 profile 찾기 : real1 이 실행 중이면 real2 를, real2 가 실행 중이면 real1
function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ "$RESPONSE_CODE" -ge 400 ]

    then
      CURRENT_PROFILE=real2
    else
      CURRENT_PROFILE=$(curl -s http://localhost/profile)

    fi

    if [ "$CURRENT_PROFILE" == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

# 실행 중이지 않은 profile 의 port 확인
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ "$IDLE_PROFILE" == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}