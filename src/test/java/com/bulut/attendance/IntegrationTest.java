package com.bulut.attendance;

import com.bulut.attendance.config.AsyncSyncConfiguration;
import com.bulut.attendance.config.EmbeddedElasticsearch;
import com.bulut.attendance.config.EmbeddedRedis;
import com.bulut.attendance.config.EmbeddedSQL;
import com.bulut.attendance.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BulutAttendanceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedElasticsearch
@EmbeddedSQL
public @interface IntegrationTest {
}
