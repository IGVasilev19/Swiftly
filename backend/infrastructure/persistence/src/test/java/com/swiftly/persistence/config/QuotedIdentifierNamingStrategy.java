package com.swiftly.persistence.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Set;

public class QuotedIdentifierNamingStrategy implements PhysicalNamingStrategy {

    private static final Set<String> RESERVED_KEYWORDS = Set.of("year", "type", "user", "order");

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return applyQuoting(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return applyQuoting(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return applyQuoting(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return applyQuoting(name, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return applyQuoting(name, jdbcEnvironment);
    }

    private Identifier applyQuoting(Identifier name, JdbcEnvironment jdbcEnvironment) {
        if (name == null) {
            return null;
        }
        
        String text = name.getText();
        if (RESERVED_KEYWORDS.contains(text.toLowerCase())) {
            return Identifier.toIdentifier(text, true);
        }
        
        return name;
    }
}
