package com.boa.tcautomation.util;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DatabaseToCsvUtil {

    private final CamelContext camelContext;
    private final ProducerTemplate producerTemplate;

    public DatabaseToCsvUtil() throws Exception {
        this.camelContext = new DefaultCamelContext();
        this.producerTemplate = camelContext.createProducerTemplate();
        camelContext.start();
    }

    public void queryToCsv(String query, String destination, String fileName) throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:queryToCsv")
                        .to("jdbc:dataSource") // Assumes a DataSource bean is configured
                        .marshal().csv()
                        .toD("file:" + destination + "?fileName=" + fileName + "&fileExist=Append");
            }
        });

        producerTemplate.sendBodyAndHeaders("direct:queryToCsv", query, Map.of(
                "destination", destination,
                "fileName", fileName
        ));
    }
}
