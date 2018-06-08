# OmH Shim Handler
## Installation
The program can be found at https://github.com/s154302/OmH-ShimHandler. It requires Java 1.8 as well as an installation of Docker. Docker can be installed from https://docs.docker.com/docker-for-windows/install/ for windows and \\ https://docs.docker.com/docker-for-mac/install/ for Mac. 

Once docker has been installed a docker-machine needs to be set up. This can be done by running docker-machine in a shell. This program has primarily been tested using Windows Powershell, but should also work for other shells. Creating a machine can be done using the command \verb|docker-machine create [machine-name]|. Once set up, the machine environment might have to be initialised, to do this run \verb|docker-machine env [machine-name]| and follow the instructions. The machine should now be up and running. To check this use the command \verb|docker-machine ls| and check the status of the machine. 

Once the machine is running it should be possible to run the shell script \newline\verb|docker-setup.sh| included here. This script will set up the  docker containers for both Shimmer and the data storage unit using docker compose, which should have been installed along with docker-machine. Before running this script, open it and ensure that the variables within are correct. These include the docker-machine's IP, which can be found using the ls command, the IP of the website this program is to be hosted on, this defaults to \verb|http://localhost:8080|, and the shim key of the provider you would like to retrieve data from along with client credentials for the application using this program. The program has currently only been tested for Nokia Health. However, it should be able to support any of the providers specified at https://github.com/openmhealth/shimmer. Finally it is possible to specify how often data should be synchronised, as explained in the document. 

## Usage
Once the program has been set up and is runnning, run the OmH shim handler application. This should be able to be compiled to a WAR file, and hosted anywhere. The current implementation hosts it on a Tomcat server, and contains a create user function, which takes a username and a password, and sets up a user for which data is synchronised. The create user button will redirect to the appropriate third party website and ask for authorisation. Once authorised it should redirect back to the web application. Once authorised the program should start synchronising data at the rate specified in the docker setup file. 
