package com.boa.tcautomation.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                // You can add global configurations here if needed
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                System.out.println("Total routes: " + camelContext.getRoutes().size());
                camelContext.getRoutes().forEach(r -> System.out.println("Route: " + r));
            }
        };
    }
}