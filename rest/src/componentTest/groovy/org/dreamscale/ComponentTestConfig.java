package org.dreamscale;

import org.dreamscale.feign.DefaultFeignConfig;
import org.dreamscale.feign.JacksonFeignBuilder;
import org.dreamscale.springboot.config.CommonSpringBootConfig;
import org.dreamscale.springboot.crud.CrudClient;
import org.dreamscale.springboot.crud.CrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Import({
        CommonSpringBootConfig.class,
        DefaultFeignConfig.class
})
@Configuration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        LiquibaseAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        MetricRepositoryAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class
})
class ComponentTestConfig {

    @Value("http://localhost:${server.port}/")
    String baseUrl;
    @Autowired
    JacksonFeignBuilder feignBuilder;

    @Bean
    CrudClient crudClient() {
        return feignBuilder.target(CrudClient.class, baseUrl);
    }

    @Bean
    CrudResource crudResource() {
        return new CrudResource();
    }

}
