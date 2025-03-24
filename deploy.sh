#!/bin/bash

echo "🔍 현재 실행 중인 Java 프로세스 확인..."
PID=$(pgrep -f '.jar')

if [ -n "$PID" ]; then
  echo "🛑 기존 프로세스 종료 (PID: $PID)..."
  kill -9 $PID
else
  echo "✅ 실행 중인 프로세스 없음"
fi

echo "🚀 최신 JAR 파일 실행..."
JAR_PATH=$(ls /root/cicd/*SNAPSHOT.jar | tail -n 1)
nohup java -jar "$JAR_PATH" > /root/nohup.out 2>&1 &
echo "✅ 배포 완료! 백그라운드 실행 중"
