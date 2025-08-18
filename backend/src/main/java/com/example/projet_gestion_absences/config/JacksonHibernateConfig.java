package com.example.projet_gestion_absences.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonHibernateConfig {

    @Bean
    public Module hibernateModule() {
        return new Hibernate6Module();
        // If you ever need options:
        // Hibernate6Module m = new Hibernate6Module();
        // m.enable(Hibernate6Module.Feature.FORCE_LAZY_LOADING); // usually avoid
        // return m;
    }
}
