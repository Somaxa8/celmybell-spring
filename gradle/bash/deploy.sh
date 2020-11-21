#!/usr/bin/env bash

docker stop celmybell
docker rm celmybell
docker rmi celmybell

cd /root/celmybell/project
bash -x gradlew buildDocker --no-daemon --stacktrace -Dprod -Pprofile=prod -x test
docker logs -f celmybell