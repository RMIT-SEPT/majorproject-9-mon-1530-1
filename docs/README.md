# SEPT Startup code and project Structure documentation

# Quick Start

## Running front-end locally

### Prerequisites

Make sure that before you try to run anything locally from the front-end, you have installed the latest version of [Node.js](https://nodejs.org/en/) (versions above 12.0.0 should suffice). You can verify that you have installed Node.js correctly by navigating to your terminal and running the following command:

```
node -v
```

Node.js will be installed if this command returns a version number.

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

# Setting up database docker image

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

After running this command the database will be started along with it's docker container, feel free to close the terminal or cancel the command because the container is running in the background.

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

------------------- rebuild database container:
docker stop sept-db
docker rm sept-db
docker build .
docker run --name sept-db -p 5432:5432 <container hash>

--------------- use PGADMIN
download and install pgadmin (postgreSql admin client)
run pgAdmin from your local machine
open a browser and go to
http://127.0.0.1:54501/browser/
Note the port might be sometimes different

add the local server to pgAdmin
name: sept-db
Connection.host:localhost
Connection.port:5432
Connection.username: postgres
Connection.password: postgres

-- to run a query in pgAdmin:
in pgadmin,
first make sure you have connected to sept-db server, as described above
click on the sept database (under databases)
at the top, click on tools, then "query tool"
you can run your sql queries from here.
you can use the database.sql file (under setup) to populate the database
----- see all tables
SELECT \*
FROM pg_catalog.pg_tables
WHERE schemaname != 'pg_catalog' AND
schemaname != 'information_schema';
