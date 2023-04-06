package com.ue.urlshortener.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = UrlPointerRepositoryTest.DataSourceInitializer.class)
class UrlPointerRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.9-alpine");

    @Autowired
    private UrlPointerRepository repository;

    private static final String TARGET = "https://www.youtube.com/watch?v=iiMFRMoxxEI";
    private static final String TARGET_ID = "SH0rT";

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new UrlPointer(TARGET, TARGET_ID),
                new UrlPointer(TARGET + "1", TARGET_ID + "1"),
                new UrlPointer(TARGET + "a", "ab6Vs"),
                new UrlPointer(TARGET + "B", "AAv5aB"),
                new UrlPointer(TARGET + "&", "ys995c7")
        ));
    }

    @Test
    void shouldFindByTargetIdentifier() {
        var actual = repository.findByTargetIdentifier(TARGET_ID);

        assertThat(actual).hasValueSatisfying(a -> {
            assertThat(a.getTargetIdentifier()).isEqualTo(TARGET_ID);
            assertThat(a.getTarget()).isEqualTo(TARGET);
            assertThat(a.getCreatedAt()).isNotNull();
        });
    }

    @Test
    void shouldFindByTarget() {
        var actual = repository.findByTarget(TARGET);

        assertThat(actual).hasValueSatisfying(a -> {
            assertThat(a.getTargetIdentifier()).isEqualTo(TARGET_ID);
            assertThat(a.getTarget()).isEqualTo(TARGET);
            assertThat(a.getCreatedAt()).isNotNull();
        });
    }


    public static class DataSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            );
        }
    }
}