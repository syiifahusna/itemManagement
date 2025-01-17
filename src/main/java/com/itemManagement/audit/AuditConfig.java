package com.itemManagement.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvidor")
public class AuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvidor(){
        return new AuditorAwareImpl();
    }
}
