package net.twilightcity;

import net.twilightcity.feign.DefaultFeignConfig;
import net.twilightcity.feign.JacksonFeignBuilder;
import net.twilightcity.springboot.config.CommonSpringBootConfig;
import net.twilightcity.springboot.rest.CrudClient;
import net.twilightcity.springboot.rest.CrudResource;
import net.twilightcity.test.BaseTestConfig;
import net.twilightcity.springboot.exception.ExceptionClient;
import net.twilightcity.springboot.exception.ExceptionResource;
import net.twilightcity.springboot.exception.ExceptionThrowingFilter;
import net.twilightcity.springboot.swagger.SwaggerClient;
import org.springframework.beans.factory.annotation.Autowired;
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
class ComponentTestConfig extends BaseTestConfig {

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

    @Bean
    ExceptionClient exceptionClient() {
        return feignBuilder.target(ExceptionClient.class, baseUrl);
    }

    @Bean
    ExceptionResource exceptionResource() {
        return new ExceptionResource();
    }

    @Bean
    ExceptionThrowingFilter exceptionThrowingFilter() {
        return new ExceptionThrowingFilter();
    }

    @Bean
    SwaggerClient swaggerClient() {
        return feignBuilder.target(SwaggerClient.class, baseUrl);
    }

}
