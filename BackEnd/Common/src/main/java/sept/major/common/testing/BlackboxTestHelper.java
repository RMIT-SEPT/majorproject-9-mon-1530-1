package sept.major.common.testing;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Used in unit testing where a clean environment is required, allowing for accurate service level testing.
 * By implementing this class in your unit test a docker container containing a postgres database will be spun up before every test.
 * This dramatically increase runtime but will ensure your tests are completely isolated from other of impacts.
 * <p>
 * By implementing this class the service this will be testing will be created on a random port.
 */
@Component
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BlackboxTestHelper {

    /**
     * A static db name for simpler properties file and external db reading
     */
    private static final String DATABASE_NAME = "sept";
    /**
     * A static username for simpler properties file and external db reading
     */
    private static final String DATABASE_USER = "postgres";
    /**
     * A static password for simpler properties file and external db reading
     */
    private static final String DATABASE_PASSWORD = "postgres";


    /**
     * The host port for the temporary test database (The port you connect to outside of the docker container).
     * The port is automatically generated to avoid port conflicts.
     * It is also made static so that the port remains the same for every test, vital because Spring context prohibits the changing of jpa connections at runtime
     */
    private static final int DATABASE_PORT = generateDatabasePort();

    /**
     * The port that the service uses. Is randomized to avoid port conflicts
     */
    @LocalServerPort
    private int servicePort;

    /*

     */
    /**
     * Object that represents the database docker container.
     * {@link Container} annotation means the container will be reset for each test.
     * The container must be initialized so that the container is constructed before the starting of Spring context.
     * It must be before Spring context so that JPA can connect to the database without error.
     */
    @Container
    private FixedHostPortPostgresContainer databaseContainer = createPostgresContainer();

    /**
     * Generates a random port that isn't already being used. Done to avoid port conflicts
     *
     * @return A random unused port number
     */
    private static int generateDatabasePort() {
        try {
            return new ServerSocket(0).getLocalPort(); // By giving 0 in ServerSocket constructor it will find a random port
        } catch (IOException e) {
            throw new RuntimeException("System had no free ports", e);
        }
    }

    /**
     * Creates and starts the database docker container.
     * Sets the spring property so that the service knows how to connect to it as well.
     *
     * @return The object that represent the docker container. Useful for container management
     */
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

    /**
     * Generates the url that tests will use to access the service being tested. Needed because the service port is randomized.
     *
     * @return The url for the service junit tests implementing this class with be testing.
     */
    public String getUrl() {
        return String.format("http://%s:%s/%s", databaseContainer.getContainerIpAddress(), servicePort, getApiExtension());
    }

    /**
     * Convenience method that calls {@link #getUrl()} and adds request parameters to the result using {@link #addRequestParameters(String, List)}
     * Used in tests when parameters need to be included in the request
     *
     * @param requestParameters The request parameters to add to the url
     * @return The url with request parameters added
     */
    public String getUrl(List<RequestParameter> requestParameters) {
        return addRequestParameters(getUrl(), requestParameters);
    }

    /**
     * Convenience method that calls {@link #getUrl(String)} and adds request parameters to the result using {@link #addRequestParameters(String, List)}
     * Used in tests when parameters need to be included in the request
     *
     * @param urlExtension      The string to extend the url with
     * @param requestParameters The request parameters to add to the url
     * @return The url with request parameters added
     */
    public String getUrl(String urlExtension, List<RequestParameter> requestParameters) {
        return addRequestParameters(getUrl(urlExtension), requestParameters);
    }


    /**
     * Adds the provided requestParameters to the provided url in the format needed for API requests.
     * For example: <url>?hoursId=1&workerUsername=bob
     *
     * @param url               The url to add request parameters to
     * @param requestParameters The request parameters to add to the url
     * @return The url with the request parameters added
     */
    public String addRequestParameters(String url, List<RequestParameter> requestParameters) {
        if (requestParameters != null && !requestParameters.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(url);
            for (int i = 0; i < requestParameters.size(); i++) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(requestParameters.get(i));
            }

            return stringBuilder.toString();
        }

        return url;
    }

    /**
     * Generates the url that tests will use to access the service being tested. Needed because the service port is randomized.
     * Is an extension of the {@link #getUrl()} method by providing a convenient way to add a url extension
     *
     * @param urlExtension The string to extend the url with
     * @return The url for the service junit tests implementing this class with be testing including the provided url extension.
     */
    public String getUrl(String urlExtension) {
        return String.format("%s/%s", getUrl(), urlExtension);
    }

    /**
     * Used when creating the database docker container. Provides the name of the sql file to run at the start up of the database.
     *
     * @return The name of the sql file to run at the start up of the database.
     */
    public abstract String getInitScriptName();

    /**
     * Used when generating url's for accessing the service unit tests implementing this class will be testing.
     * It is the extension used to represent the service, i.e /hours
     *
     * @return It is the extension used to represent the service, i.e /hours. Note: Do not include the "/" in the extension.
     */
    public abstract String getApiExtension();

}
