L1JTW Server
===

This repo is forked from [l1j-tw-99nets](https://github.com/nonono44/l1j-tw-99nets), now you could simply launch l1jtw server by docker.

## Usage

Create `.env`

```
cp .env.example .env
```

Start specific version of server

```
docker-compose up v350c
docker-compose up v380c
```

## Build Server

Setup development environment

```
docker-compose up dev
```

Execute shell inside dev container

```
docker-compose exec dev sh
```

Start compilation

```
make
```
