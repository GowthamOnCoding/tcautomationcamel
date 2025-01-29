package com.boa.tcautomation.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TestRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://testTimer?period=5000")
                .routeId("testRoute")
                .setBody(constant("Hello, Camel!"))
                .log("Test Route Body: ${body}");
    }
}
