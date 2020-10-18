# SEPT Startup code and project Structure documentation

# Project Structure
### ~/.circleci
The location of our circleCi configuration

### ~/Backend
The location of all the back end services and the common library. Each of these services have their own folder named after the service it contains. 
Each service folder is a spring boot project with the typical file structure you would find in a spring boot project. 

### ~/FrontEnd
The location of the project\'s front end. This file structure within this folder is a typical react project structure.

### ~/Docs
Here you find various documentation related to the project. The first two files are the Api Structure.png and this is simple layout of the project\'s backend.
The next file is this file (The startup guide and project structure)
 #### ~/Docs/Milestone 2 Submision
 Here is all the documentation we attached to our milestone 2 submission. Including meeting minutes up to the milestone 2 and other submission details
 #### ~/Docs/Swagger
 In this folder you will find our initial designs for the various backend services. These swaggers have been depricated since Milestone 1 however they are kept here for history. 
 
 ### ~/Setup
Contains various filse for local and cloud setup
 #### ~/Setup/beanstalk_deployment
 Contains the configuration for our various Amazon Beanstalks 
 #### ~/Setup/docker/db
 The folder which the database docker container is constructed. It contains the docker file which details the docker construction steps and the start up SQL applied to the database. 




# Quick Start

## Setting up database docker image

For our project we currently use a docker image for our database.

The following sets assume you have docker installed on your system. Visit https://www.docker.com/products/docker-desktop for the installation

To set it up do the following:

Open a command prompt (power shell for windows) in setup/docker/db and run

```
docker build .
```

Once finished it will print out the id for the image, for example my id was f9ba4c44bcf0:

```
Successfully built f9ba4c44bcf0
```

Copy this id because we will use it in the following steps.

**Note**: You might get the following security warning. It is not a problem and is expected

```
SECURITY WARNING: You are building a Docker image from Windows against a non-Windows Docker host. All files and directories added to build context will have '-rwxr-xr-x' permissions. It is recommended to double check and reset permissions for sensitive files and directories.
```

Now that we have the id we run the following.

Here we are naming the container "sept-db" and hosting the database on port 5432.

Feel free to change the container name if you wish and if you wish to change the port change the second number and leave the first. For example "5432:25565".

Changing the first number will cause the database to be unreachable.

```
docker run --name sept-db -p 5432:5432 465e35a9dbd9
```

After running this command the database will be started along with it's docker container, feel free to close the terminal or cancel the command because the container is running in the background.\

## Running back-end locally

### Prerequisites

Make sure you have the latest version of maven installed. You can check using the following:
```mvn install```
If you don't have maven installed please visit https://maven.apache.org/ and install. 

**You will also need the docker database image setup, a guide to set this is up is provided above.**

### Installing / Run order
It is vital that you run and build the back end services in a specific order. Some services have dependencies and failing to get them will cause the service not run. The order is common, users, hours, bookings and then Availability or simply follow the following steps in order:

### Building the Common library
First we must enter the Common library folder located at BackEnd/Common. If you are in the root then use this command:
``` cd BackEnd/Common ```
Now that we are in the common folder use the following command to build it: 
``` mvn install ```
A new target folder should be created with a java file called common-1.1.7.jar.
The common library has been built

### Building the Users service
First we must enter the User service folder located at BackEnd/Users. If you are in the root then use this command:
``` cd BackEnd/Users ```
Now that we are in the user folder use the following command to build it: 
``` mvn install ```
A new target folder should be created with a java file called users-1.0.0.jar
You should then run this java file with the following command:
``` java -jar users-1.0.0.jar ```
Keep this terminal open so that the services remains running.

### Building the Hours service
First we must enter the Hours service folder located at BackEnd/Hours. If you are in the root then use this command:
``` cd BackEnd/Hours ```
Now that we are in the hours folder use the following command to build it: 
``` mvn install ```
A new target folder should be created with a java file called hours-1.0.0.jar
You should then run this java file with the following command:
``` java -jar hours-1.0.0.jar ```
Keep this terminal open so that the services remains running.

### Building the Bookings service
First we must enter the Bookings service folder located at BackEnd/Bookings. If you are in the root then use this command:
``` cd BackEnd/Bookings ```
Now that we are in the Bookings folder use the following command to build it: 
``` mvn install ```
A new target folder should be created with a java file called bookings-1.0.0.jar
You should then run this java file with the following command:
``` java -jar bookings-1.0.0.jar ```
Keep this terminal open so that the services remains running.

### Building the Availability service
First we must enter the Availability service folder located at BackEnd/Availability. If you are in the root then use this command:
``` cd BackEnd/Availability ```
Now that we are in the Availability folder use the following command to build it: 
``` mvn install ```
A new target folder should be created with a java file called availability-1.0.0.jar
You should then run this java file with the following command:
``` java -jar availability-1.0.0.jar ```
Keep this terminal open so that the services remains running.


## Running front-end locally

### Prerequisites

Make sure that before you try to run anything locally from the front-end, you have installed the latest version of [Node.js](https://nodejs.org/en/) (versions above 12.0.0 should suffice). You can verify that you have installed Node.js correctly by navigating to your terminal and running the following command:

```
node -v
```

Node.js will be installed if this command returns a version number.

**The back end should also be running, without the back end many features will be missing. See how the instructions to build the back end above.**

### Starting the web pages locally

Navigate to the directory on your machine where you have cloned this repository. Then, change directories into the `FrontEnd` folder by running:

```
cd FrontEnd/
```

Once you are in the `FrontEnd` directory, you will need to run the following:

```
npm install
```

`npm` stands for "Node Package Manager", and comes installed automatically alongside Node.js. What we are doing here is simply fetching a list of project dependencies from the `package.json` file, and downloading them from the npm repository online. Without these dependencies, you could not run the project. Give that a minute or so to finish.

If, for some reason, the command failed when trying to install dependencies, there are a few things you could try:

- Delete `package-lock.json` file and `node_modules` folder if they exist.
- Ensure you have the latest version of Node.js.
- Try to run the install commmand again.

Once the command finishes, run the following to start the app:

```
npm run start
```

The following command calls the `start` script from the `package.json` file, which will start the application locally. You can find the application running at [http://localhost:3000/](http://localhost:3000/).

Once you are ready to stop the application, simply terminate the command from your terminal.

# Database docker image management

To see if the docker container is still running use

```angular2
docker ps
```

To stop the docker container use the following. Note that sept-db should be replaced with your name if you changed it

```
docker stop sept-db
```

To start the docker container use the following. This command will only work if the container is setup, see the above section to do this. Note that sept-db should be replaoced with your name if you changed it.

```
docker start sept-db
```

#### Rebuilding database container:
Stop the container
```docker stop sept-db```
Remove the container
```docker rm sept-db```

Rebuild  the docker image using the Setting up database docker image instructions above.
Start a container for the new image
```docker run --name sept-db -p 5432:5432 <container hash>```

#### Database connection details
host: localhost
port: 5432
username: postgres
password: postgres
