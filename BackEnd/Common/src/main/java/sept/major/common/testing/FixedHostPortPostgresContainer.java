package sept.major.common.testing;

import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * The following class is an almost direct duplicate of the FixedHostPortGenericContainer class created by the testContainers library.
 * I chose to duplicate the class so that I can make use of PostgresSQLContainer functionality while still having a fixed host port.
 * A class is needed to set a fix port because the developers of the testContainers library made the setFixedExposedPort method protected,
 * this is done to circumvent port conflicts however we need a fixed port due to Spring context prohibiting JPA connections to
 * change in runtime without predetermined properties; Instead of a random port we will provide one.
 *
 * @param <SELF> This class, inherited from {@link PostgreSQLContainer}
 */
public class FixedHostPortPostgresContainer<SELF extends FixedHostPortPostgresContainer<SELF>> extends PostgreSQLContainer<SELF> {

    /**
     * Bind a fixed TCP port on the docker host to a container port
     * @param hostPort          a port on the docker host, which must be available
     * @return                  this container
     */
    public SELF withFixedExposedPort(int hostPort) {

        return withFixedExposedPort(hostPort, InternetProtocol.TCP);
    }

    /**
     * Bind a fixed port on the docker host to a container port
     * @param hostPort          a port on the docker host, which must be available
     * @param protocol          an internet protocol (tcp or udp)
     * @return                  this container
     */
    public SELF withFixedExposedPort(int hostPort, InternetProtocol protocol) {

        super.addFixedExposedPort(hostPort, POSTGRESQL_PORT, protocol);

        return self();
    }
}
