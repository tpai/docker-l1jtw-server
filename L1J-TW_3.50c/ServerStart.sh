#!/bin/bash
# Program:
#     The program will show it's name and first 3 parameters.
# History:
# 2005/08/25 VBird First release
# 2016/02/21 tonypai Add automated build

PATH=.:/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/local/jdk1.7.0/bin
export PATH

# Import data to sql
/etc/init.d/mysql start
echo "create database l1jdb;" | mysql -u root
mysql -u root l1jdb < /data/db/l1jdb_Taiwan.sql

# Run Server
java -Xmx512m -Xincgc -jar l1jserver.jar
