#!/bin/bash

PATH=.:/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/local/jdk1.7.0/bin
export PATH

if [ $(echo "show databases;" | mysql -h $DB_HOST -u root -p$DB_PWD | grep "l1jdb") = "l1jdb" ]; then
    echo "Database l1jdb exist!"
else
    echo "create database l1jdb;" | mysql -h $DB_HOST -u root -p$DB_PWD
    mysql -h $DB_HOST -u root -p$DB_PWD l1jdb < /data/db/l1jdb_Taiwan.sql
fi

cd /data/config/

# DB Related
sed 's/mysql:\/\/localhost/mysql:\/\/'"$DB_HOST"'/g' server.properties > custom.server; cat custom.server > server.properties
sed 's/Password=password/Password='"$DB_PWD"'/g' server.properties > custom.server; cat custom.server > server.properties

# Exp Rate
sed 's/RateXp = 1.0/RateXp = '"$RATE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/RateLawful = 1.0/RateLawful = '"$RATE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/RateKarma = 1.0/RateKarma = '"$RATE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/RateDropAdena = 1.0/RateDropAdena = '"$RATE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/RateDropItems = 1.0/RateDropItems = '"$RATE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties

# Enchant Chance
sed 's/EnchantChanceWeapon = 0/EnchantChanceWeapon = '"$ENCHANT_CHANCE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/EnchantChanceArmor = 0/EnchantChanceArmor = '"$ENCHANT_CHANCE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties
sed 's/AttrEnchantChance = 10/AttrEnchantChance = '"$ENCHANT_CHANCE"'/g' rates.properties > custom.rates; cat custom.rates > rates.properties

# Unzip Maps
unzip /data/maps/352_maps.zip -d /data/maps/

cd /data

java -Xmx512m -Xincgc -jar /data/l1jserver.jar
