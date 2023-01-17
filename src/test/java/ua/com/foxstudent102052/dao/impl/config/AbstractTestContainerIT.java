package ua.com.foxstudent102052.dao.impl.config;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@JdbcTest
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
public abstract class AbstractTestContainerIT {
    private static final String POSTGRES_VERSION = "postgres:15";

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withDatabaseName("tests-db")
            .withUsername("sa")
            .withPassword("sa");
    }

    public static void start() {
        postgreSQLContainer.start();

        System.setProperty("DATASOURCE_CLASS_NAME", postgreSQLContainer.getDriverClassName());
        System.setProperty("DATASOURCE_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DATASOURCE_USER", postgreSQLContainer.getUsername());
        System.setProperty("DATASOURCE_PASSWORD", postgreSQLContainer.getPassword());
    }

    public static void close() {
        postgreSQLContainer.close();
    }
}
