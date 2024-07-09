### 은근슨물 Spring Boot API Project

### 빌드 방법
#### 1. 환경변수 설정
- application-prod.yml 에 database 설정 및 oauth 설정을 변경해줍니다. 
- 만약 Jasypt 를 사용하는 경우, 암호화된 값을 ENC() 안에 넣어서 환경변수를 설정합니다.
```yml
# 예시
...
  kakao:
    client-id: ENC(8yw6AScqYugrdE0u+xdcrOW8/O0IAyx+z9X3Gd6kPKWkMK5qnK5DtT2troDKNVWJ)
```
<br/>

#### 2. JAR 파일 빌드
- 다음과 같이 JAR 파일을 빌드합니다.
```sh
key=jasypt-key # 위에서 jasypt를 사용한 경우 암호화 시 사용한 key
./gradlew clean build --scan -Pprofile=prod --scan -Pjasypt.encryptor.password=${key}
```
<br/>

#### 3. Docker Compose 를 이용하여 실행

- 다음과 같이 docker compose 를 실행합니다. 

```sh
docker compose -f docker-compose-green.yml -p ugsm-green up -d
```
