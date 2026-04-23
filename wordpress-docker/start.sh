#!/bin/bash

if [ ! -f .env ]; then
  cp .env.example .env

  MYSQL_PASSWORD=$(openssl rand -base64 16 | tr -d '\n')
  MYSQL_ROOT_PASSWORD=$(openssl rand -base64 16 | tr -d '\n')

  sed -i "s/^MYSQL_PASSWORD=.*/MYSQL_PASSWORD=$MYSQL_PASSWORD/" .env
  sed -i "s/^MYSQL_ROOT_PASSWORD=.*/MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD/" .env

  echo ".env créé automatiquement avec mots de passe générés."
fi

docker compose up -d