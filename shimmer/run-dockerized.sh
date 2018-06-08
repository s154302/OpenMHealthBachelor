#!/bin/bash

BASEDIR=`pwd`

isNpmPackageInstalled() {
  npm list --depth 1 -g $1 > /dev/null 2>&1
}

# check dependencies
if ! hash "npm" 2>/dev/null;
then
    echo "npm can't be found"
    exit 1
fi

if ! hash "docker-compose" 2>/dev/null;
then
    echo "docker-compose can't be found"
    exit 1
fi

# remove the symlink which may have been created by running natively earlier
rm -f ${BASEDIR}/shim-server/src/main/resources/public #CMD

# build the backend
echo Building the resource server...
cd ${BASEDIR} #CMD
./gradlew build #CMD

# run the containers
cd ${BASEDIR} #CMD
echo Building the containers...
docker-compose -f docker-compose-build.yml build #CMD

echo Starting the containers in the background...
docker-compose -f docker-compose-build.yml up -d #CMD

echo Done, containers are starting up and may take up to a minute to be ready.

read -p "Press [Enter] to continue"