package com.boa.tcautomation.route;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.service.DatabaseService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TcMasterRoute extends RouteBuilder {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public void configure() {
        from("direct:addTcMaster")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.addRow("tc_master", rowData);
                });

        from("direct:deleteTcMaster")
                .process(exchange -> {
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    databaseService.deleteRow("tc_master", "tc_id", tcId);
                });

        from("direct:updateTcMaster")
                .process(exchange -> {
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    Map<String, Object> updatedData = exchange.getIn().getBody(Map.class);
                    databaseService.updateRow("tc_master", "tc_id", tcId, updatedData);
                });

        from("direct:cloneTcMaster")
                .process(exchange -> {
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    databaseService.cloneRow("tc_master", "tc_id", tcId);
                });

        from("direct:insertTcMaster")
                .process(exchange -> {
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    databaseService.insertRow("tc_master", rowData);
                });

        from("direct:selectTcMaster")
                .process(exchange -> {
                    String tcId = exchange.getIn().getHeader("tcId", String.class);
                    TcMaster result = databaseService.selectRow("tc_master", "tc_id", tcId, TcMaster.class);
                    exchange.getIn().setBody(result);
                });
    }
}
