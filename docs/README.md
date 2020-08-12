# SEPT Startup code and  project Structure documentation 

# Quick Start


# Setting up database docker image 
For our project we currently use a docker image for our database.
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