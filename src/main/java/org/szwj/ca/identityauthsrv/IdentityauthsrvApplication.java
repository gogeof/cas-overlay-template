package org.szwj.ca.identityauthsrv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "org.szwj.ca.identityauthsrv.dao")
public class IdentityauthsrvApplication extends SpringBootServletInitializer implements
    EmbeddedServletContainerCustomizer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IdentityauthsrvApplication.class);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
    }

    public static void main(String[] args) {
        SpringApplication.run(IdentityauthsrvApplication.class, args);
    }
}
