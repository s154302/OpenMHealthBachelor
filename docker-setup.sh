#!/bin/bash

# -------------------------------
# --- CONFIGURATION VARIABLES ---
# -------------------------------

MAIN_URL=192.168.99.100
SHIMMER_DATA_PROVIDER_REDIRECT_BASE_URL=http://192.168.99.100:8084
SHIMMER_CLIENT_REDIRECT=http://localhost:8080

CLIENT_USERNAME=testClient
CLIENT_SECRET=testClientSecret

WITHINGS_CLIENT_ID=b26681415595f12395355f32f276785f80574c2ec313b0e4b8830cbc872
WITHINGS_CLIENT_SECRET=78b2453b93c1951e8f677b97d64ce58b504085e074f1b2e797360c19d43b4

FITBIT_CLIENT_ID=22CTNT
FITBIT_CLIENT_SECRET=26e5b32c0262dd681ed8899d644c2aef

MISFIT_CLIENT_ID=2dkX0MfWRT2TPyDA
MISFIT_CLIENT_SECRET=EvzCmwTXGbvKQWzCmGmZFQLJEUjuDw2g

SHIM_KEY=withings

# -------------------------------

BASEDIR=`pwd`
RED='\033[0;31m'
NC='\033[0m'

echo -e ${RED}Make sure to edit the config values in this file for the program to work correctly!${NC}

# Generate config.properties file
echo Generating the config file...
cd src/main/resources
rm -f config.properties
cat <<EOT >> config.properties
# GENERAL PROPERTIES
clientUsername = ${CLIENT_USERNAME}
clientSecret = ${CLIENT_SECRET}
shimkey = ${SHIM_KEY}
authenticationPort = 8082
resourcePort = 8083
shimmerPort = 8084
localPort = 8080
mainurl = 192.168.99.100

# Set value of each attribute in order to set how often to synchronise
# 0 = constant, 1 = daily, 2 = weekly
# DATA TYPES
blood_pressure = 0
body_height = 0
body_weight = 0
calories_burned = 0
body_temperature = 0
heart_rate = 0
sleep_duration = 0
sleep_episode = 0
step_count = 0
EOT

echo Done!

cd ../../../shimmer

echo Creating the resource server file...
rm -f resource-server.env

cat <<EOT >> resource-server.env
OPENMHEALTH_SHIMMER_DATA_PROVIDER_REDIRECT_BASE_URL=${SHIMMER_DATA_PROVIDER_REDIRECT_BASE_URL}
OPENMHEALTH_SHIMMER_CLIENT_REDIRECT_URL=${SHIMMER_CLIENT_REDIRECT}

OPENMHEALTH_SHIM_FITBIT_CLIENT_ID=${FITBIT_CLIENT_ID}
OPENMHEALTH_SHIM_FITBIT_CLIENT_SECRET=${FITBIT_CLIENT_SECRET}
OPENMHEALTH_SHIM_FITBIT_INTRADAY_DATA_AVAILABLE=false
#OPENMHEALTH_SHIM_FITBIT_INTRADAY_DATA_GRANULARITY_IN_MINUTES=1

#OPENMHEALTH_SHIM_GOOGLEFIT_CLIENT_ID=set-value-here
#OPENMHEALTH_SHIM_GOOGLEFIT_CLIENT_SECRET=set-value-here

#OPENMHEALTH_SHIM_IHEALTH_CLIENT_ID=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_CLIENT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_SANBOXED=false
#OPENMHEALTH_SHIM_IHEALTH_CLIENT_SERIAL_NUMBER=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_ACTIVITY_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_BLOOD_GLUCOSE_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_BLOOD_PRESSURE_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_SLEEP_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_SP_O2_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_SPORT_ENDPOINT_SECRET=set-value-here
#OPENMHEALTH_SHIM_IHEALTH_WEIGHT_ENDPOINT_SECRET=set-value-here

#OPENMHEALTH_SHIM_JAWBONE_CLIENT_ID=set-value-here
#OPENMHEALTH_SHIM_JAWBONE_CLIENT_SECRET=set-value-here

#OPENMHEALTH_SHIM_MISFIT_CLIENT_ID=${MISFIT_CLIENT_ID}
#OPENMHEALTH_SHIM_MISFIT_CLIENT_SECRET=${MISFIT_CLIENT_SECRET}

#OPENMHEALTH_SHIM_MOVES_CLIENT_ID=set-value-here
#OPENMHEALTH_SHIM_MOVES_CLIENT_SECRET=set-value-here

#OPENMHEALTH_SHIM_RUNKEEPER_CLIENT_ID=set-value-here
#OPENMHEALTH_SHIM_RUNKEEPER_CLIENT_SECRET=set-value-here

OPENMHEALTH_SHIM_WITHINGS_CLIENT_ID=${WITHINGS_CLIENT_ID}
OPENMHEALTH_SHIM_WITHINGS_CLIENT_SECRET=${WITHINGS_CLIENT_SECRET}
OPENMHEALTH_SHIM_WITHINGS_INTRADAY_DATA_AVAILABLE=false
EOT

echo Done!

echo Creating setup file for DSU client DB...

cd ../dsu/resources/rdbms/postgresql
rm -f oauth2-ddl.sql

cat <<EOT >> oauth2-ddl.sql
\c omh

CREATE TABLE oauth_access_token (
  token_id          VARCHAR(256),
  token             BYTEA,
  authentication_id VARCHAR(256),
  user_name         VARCHAR(256),
  client_id         VARCHAR(256),
  authentication    BYTEA,
  refresh_token     VARCHAR(256)
);

CREATE TABLE oauth_refresh_token (
  token_id       VARCHAR(256),
  token          BYTEA,
  authentication BYTEA
);

CREATE TABLE oauth_client_details (
  client_id               VARCHAR(256) PRIMARY KEY,
  resource_ids            VARCHAR(256),
  client_secret           VARCHAR(256),
  scope                   VARCHAR(256),
  authorized_grant_types  VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities             VARCHAR(256),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  VARCHAR(4096),
  autoapprove             VARCHAR(256)
);

INSERT INTO oauth_client_details 
( client_id,
   client_secret,
   scope,
   resource_ids,
   authorized_grant_types,
   authorities
)
VALUES (
  '${CLIENT_USERNAME}',
  '${CLIENT_SECRET}',
  'read_data_points,write_data_points,delete_data_points',
  'dataPoints',
  'authorization_code,implicit,password,refresh_token',
  'ROLE_CLIENT'
);
EOT

# check dependencies
if ! hash "docker-compose" 2>/dev/null;
then
    echo "docker-compose can't be found"
    exit 1
fi

cd ../../../../
ls

# remove the symlink which may have been created by running natively earlier
rm -f shimmer/shim-server/src/main/resources/public #CMD

cd shimmer #CMD
echo Building the containers...
docker-compose up -d resourceserver

echo Done, containers are starting up and may take up to a minute to be ready.

echo Setting up the data storage unit
cd ../dsu
docker-compose -f docker-compose-init-postgres.yml up -d #CMD

echo Containers are now being set up, and should be ready in a minute

read -p "Press [Enter] to continue..."