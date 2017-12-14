package org.example.gogeof.random.config;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.example.gogeof.random.RandomController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("randomConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class RandomConfiguration {
    //注册bean到spring容器
    @Bean
    @ConditionalOnMissingBean(name = "randomController")
    public RandomController captchaController() {
        return new RandomController();
    }
}
