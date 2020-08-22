package sept.major.hours.blackbox;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerFactory extends PostgreSQLContainer<PostgresContainerFactory> {
    private static PostgresContainerFactory container;

    public static PostgresContainerFactory getContainer() {
        if (container == null) {
            container = new PostgresContainerFactory().withInitScript("hour.sql");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
