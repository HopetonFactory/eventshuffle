package fi.hopeton.config;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
    @Bean
    public DefaultConfigurationCustomizer configurationCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
                .withRenderQuotedNames(
                        RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED
                );
    }

    @Bean
    public Settings jooqSettings() {
        return new Settings().withExecuteWithOptimisticLockingExcludeUnversioned(true).withUpdateRecordVersion(true);
    }
}