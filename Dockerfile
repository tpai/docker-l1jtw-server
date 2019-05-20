FROM openjdk:8-jdk-alpine AS dev

# compilation tools
RUN apk update && \
    apk add build-base gcc

FROM openjdk:8-jre-alpine AS base

# wait-for-it.sh needs bash
RUN apk update && \
    apk add bash mysql-client

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
