# OmH Shim Handler
## OmH DSU
https://github.com/openmhealth/omh-dsu-ri
### Set up User for OmH DSU
Simple web application to be used for testing an OmH implementation.

When setting up omh-dsu-ri (the open mHealth database) you have to use the following command to build it in docker:

```
docker-compose -f docker-compose-init-postgres.yml up -d
```
Then you will have to access the container to modify the postgres database. This is achieved with the following command.
```
docker exec -it omhdsuri_postgres_1 bash 
```
The next step is to login to the postgres omh database which contains user credentials. This is done by using the following command: 
```
psql -d omh -U postgres
```
A user should then be added using the following script: https://github.com/openmhealth/omh-dsu-ri/blob/master/resources/rdbms/common/oauth2-sample-data.sql .

Now data can be added and accessed through the omh-webapp

## Shimmer

https://github.com/openmhealth/shimmer

Make sure to have a docker machine running before attempting any of the below. 
For this project the default docker machine was used.

When building shimmer, make sure only to build the resource server, as the console uses same ports as OmH DSU resource server.
This is done by running the command 
```
docker-compose up -d resourceserver
```
### Build Shimmer changes
Make sure to have bower, grunt, and nodeJS installed.
Run
```
./run-dockerized.sh
```
in a bash terminal in order to build. 
If not making any changes simply set up docker as usual. 

### Docker
Create and run docker containers
```
docker-compose up -d
```
Remove docker containers
```
docker-compose down
```
View all current containers
```
docker ps -a
```
View all current machines
```
docker-machine ls
```
