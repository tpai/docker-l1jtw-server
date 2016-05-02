#!/bin/bash

PATH=.:/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/local/jdk1.7.0/bin
export PATH

cd /data/config/

sed 's/mysql:\/\/localhost/mysql:\/\/'"$DB_HOST"'/g' server.properties > custom.server; \
cat custom.server > server.properties

sed 's/Password=password/Password='"$DB_PWD"'/g' server.properties > custom.server; \
cat custom.server > server.properties

unzip /data/maps/352_maps.zip -d /data/maps/

cd /data

java -Xmx512m -Xincgc -jar /data/l1jserver.jar
