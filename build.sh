#!/bin/zsh

./gradlew clean build -Pprofile="$1"
docker build -t kkh147173/ugsm:0.0.1-"$1" .
docker push kkh147173/ugsm:0.0.1-"$1"