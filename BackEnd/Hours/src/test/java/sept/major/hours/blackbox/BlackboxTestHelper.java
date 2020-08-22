package sept.major.hours.blackbox;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Component
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BlackboxTestHelper {

    private final String DATABASE_DOCKER_IMAGE = "postgres:13"; // The docker image which the temporary test database will be constructed from. Image is pulled from docker hub
    private final String DATABASE_NAME = "sept";
    private final String DATABASE_USER = "postgres";
    private final String DATABASE_PASSWORD = "postgres";
    private final String DATABASE_INIT_SCRIPT_LOCATION = "/docker-entrypoint-initdb.d";

    /*
        The host port for the temporary test database (The port you connect to outside of the docker container).
         Unfortunately needs to be hardcoded due to sprint context prohibiting JPA connections to be refreshed in runtime
     */
    private final int DATABASE_PORT = 5433;
    private final int CONTAINER_PORT = 5432; //The port the database uses within the docker container

    @LocalServerPort
    private int servicePort; // The port that the service uses. Is randomized to avoid port conflicts

    /*
        Object that represents the database docker container.
        @Container annotation means the container will be reset for each test.
        The container must be initialized so that the container is constructed before the starting of Spring context.
        It must be before Spring context so that JPA can connect to the database without error.
     */
    @Container
    private FixedHostPortGenericContainer databaseContainer = createPostgresContainer();

    private FixedHostPortGenericContainer createPostgresContainer() {
        /*
            Creates the docker container based on provided docker image.
            FixedHostPortGenericContainer must be used because all other containers will automatically generate a host port.
            An auto generating port is problem due to sprint context prohibiting JPA connections to be refreshed in runtime.
         */
        FixedHostPortGenericContainer databaseContainer = new FixedHostPortGenericContainer(DATABASE_DOCKER_IMAGE);
        databaseContainer.withFixedExposedPort(DATABASE_PORT, CONTAINER_PORT); // Maps the container port to the host port
        // Move the startup script to the start up script location so that the database runs the script on start up.
        databaseContainer.withCopyFileToContainer(MountableFile.forClasspathResource(getInitScriptName()), String.format("%s/%s", DATABASE_INIT_SCRIPT_LOCATION, getInitScriptName()));

        /*
            Causes the container to wait until the database prints it is ready to accept connections before continuing the application.
            If it takes longer then 60 seconds to be ready for connections then it will timeout
         */
        databaseContainer.waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2).withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS)));


        HashMap<String, String> environmentVariables = new HashMap<>();
        environmentVariables.put("POSTGRES_DB", DATABASE_NAME);
        environmentVariables.put("POSTGRES_USER", DATABASE_USER);
        environmentVariables.put("POSTGRES_PASSWORD", DATABASE_PASSWORD);
        databaseContainer.withEnv(environmentVariables);
        databaseContainer.start();

        return databaseContainer;
    }

    public String getUrl() {
        return String.format("http://%s:%s/%s", databaseContainer.getContainerIpAddress(), servicePort, getApiExtension());
    }

    public String getUrl(String urlExtension) {
        return String.format("http://%s:%s/%s/%s", databaseContainer.getContainerIpAddress(), servicePort, getApiExtension(), urlExtension);
    }

    public abstract String getInitScriptName();

    public abstract String getApiExtension();

}
