package com.vladkostrov.usermicroserviceapp.it;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractRestControllerBaseTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;


    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("user-service_testcontainers");
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void DynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
    }
}
