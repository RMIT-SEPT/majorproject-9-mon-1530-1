package sept.major.common.testing;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.ServerSocket;

@Component
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BlackboxTestHelper {

    private static final String DATABASE_NAME = "sept";  // We use static db name for simpler properties file and external db reading
    private static final String DATABASE_USER = "postgres"; // We use static username for simpler properties file and external db reading
    private static final String DATABASE_PASSWORD = "postgres"; // We use static password for simpler properties file and external db reading

    /*
        The host port for the temporary test database (The port you connect to outside of the docker container).
        The port is automatically generated to avoid port conflicts.
        It is also made static so that the port remains the same for every test, vital because Spring context prohibits the changing of jpa connections at runtime
     */
    private static final int DATABASE_PORT = generateDatabasePort();

    @LocalServerPort
    private int servicePort; // The port that the service uses. Is randomized to avoid port conflicts

    /*
        Object that represents the database docker container.
        @Container annotation means the container will be reset for each test.
        The container must be initialized so that the container is constructed before the starting of Spring context.
        It must be before Spring context so that JPA can connect to the database without error.
     */
    @Container
    private FixedHostPortPostgresContainer databaseContainer = createPostgresContainer();

    // Generates a random port that isn't already being used. Done to avoid port conflicts
    private static int generateDatabasePort() {
        try {
            return new ServerSocket(0).getLocalPort(); // By giving 0 in ServerSocket constructor it will find a random port
        } catch (IOException e) {
            throw new RuntimeException("System had no free ports", e);
        }
    }

    private FixedHostPortPostgresContainer createPostgresContainer() {
        FixedHostPortPostgresContainer databaseContainer = new FixedHostPortPostgresContainer();
        databaseContainer.withFixedExposedPort(DATABASE_PORT);
        databaseContainer.withDatabaseName(DATABASE_NAME);
        databaseContainer.withUsername(DATABASE_USER);
        databaseContainer.withPassword(DATABASE_PASSWORD);
        databaseContainer.withInitScript(getInitScriptName());
        databaseContainer.start();

        /*
            Sets a system property so that it can be accessed in the properties file.
            Needs to be accessible by the properties file so that JPA will connect to the correct port.
         */
        System.setProperty("DB_URL", databaseContainer.getJdbcUrl());

        return databaseContainer;
    }

    // Generates the url that tests will use to access the service being tested. Needed because the service port is randomized.
    public String getUrl() {
        return String.format("http://%s:%s/%s", databaseContainer.getContainerIpAddress(), servicePort, getApiExtension());
    }

    // Generates the url that tests will use to access the service being tested. Needed because the service port is randomized.
    public String getUrl(String urlExtension) {
        return String.format("http://%s:%s/%s/%s", databaseContainer.getContainerIpAddress(), servicePort, getApiExtension(), urlExtension);
    }

    public abstract String getInitScriptName();

    public abstract String getApiExtension();

}
