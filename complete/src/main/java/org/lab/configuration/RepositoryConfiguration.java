package org.lab.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration for the repository managing persistence.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"org.lab"})
@EnableJpaRepositories(basePackages = {"org.lab.repositories"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
