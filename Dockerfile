# L1J-TW Server Dockerfile

FROM mysql:5.6
MAINTAINER Tony Pai <tonypai@ifalo.com.tw>

RUN apt-get -y update
RUN apt-get -y install openjdk-7-jdk

WORKDIR /data
VOLUME /data

# Copy whole repo to /data
COPY L1J-TW_3.50c /data

EXPOSE 2000

# Start L1J-TW server
CMD sh /data/ServerStart.sh
