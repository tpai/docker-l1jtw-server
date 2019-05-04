FROM openjdk:8-jdk-alpine AS dev

# compilation tools
RUN apk update
RUN apk add build-base gcc

FROM openjdk:8-jre-alpine AS base

# wait-for-it.sh needs bash
RUN apk update
RUN apk add bash mysql-client

ENV DB_HOST 127.0.0.1
ENV DB_PWD admin
ENV RATE 10
ENV ENCHANT_CHANCE 40

FROM base AS v350c

WORKDIR /data

COPY L1J-TW_3.50c /data
EXPOSE 2000

CMD bash /data/ServerStart.sh

FROM base AS v380c

WORKDIR /data

COPY L1J-TW_3.80c /data
EXPOSE 2000

CMD bash /data/ServerStart.sh
