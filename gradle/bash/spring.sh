#!/usr/bin/env bash

docker network create celmybell
#docker run --name celmybelldb -d --network celmybell -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=celmybelldb -v /root/celmybell/mysql:/var/lib/mysql --restart always mariadb --character-set-server=utf8 --collation-server=utf8_general_ci
#docker run --name celmybellmongo -d --network celmybell -p 27017:27017 -v /root/celmybell/mongo/:/data/db -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=1234 --restart always mongo

#docker run --name ${project}mongoclient -d -p 3000:3000 --restart always --link ${project}mongo mongoclient/mongoclient