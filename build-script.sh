#!/bin/zsh

echo -e "\033[0;32m"Start building Spring Project"\033[0;32m"
./gradlew clean build --scan -Pprofile=prod --scan -Pjasypt.encryptor.password=qkralfospt.03
echo -e "\033[0;32m"Building Kotlin Spring Project completed"\033[0;32m"
echo -e "\033[0;32m"Start building docker file..."\033[0;32m"
docker build --platform=linux/amd64 --tag kkh147173/ugsm:0.0.1-prod .
echo -e "\033[0;32m"Building docker image completed!"\033[0;32m"
echo -e "\033[0;32m"Start pushing docker file..."\033[0;32m"
docker push kkh147173/ugsm:0.0.1-prod
echo -e "\033[0;32m"Pushing docker image completed!"\033[0;32m"