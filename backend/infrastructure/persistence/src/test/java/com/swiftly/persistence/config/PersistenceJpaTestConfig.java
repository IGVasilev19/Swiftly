package com.swiftly.persistence.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.swiftly.persistence.entities")
@EnableJpaRepositories("com.swiftly.persistence")
public class PersistenceJpaTestConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> {
            properties.put(AvailableSettings.DEFAULT_SCHEMA, "public");
            properties.put("hibernate.hbm2ddl.auto", "create-drop");
            properties.put("hibernate.physical_naming_strategy", QuotedIdentifierNamingStrategy.class.getName());
        };
    }
}