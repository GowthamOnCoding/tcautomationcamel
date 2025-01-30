package com.boa.tcautomation.route;

import com.boa.tcautomation.model.AitScanWindow;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.SshUtil;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomRoute extends RouteBuilder {

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private SshUtil sshUtil;

    @Override
    public void configure() {
        // 1. Delete and insert data into AIT_SCAN_WINDOW table
        from("direct:deleteAndInsertAitScanWindow")
                .process(exchange -> {
                    String aitNo = exchange.getIn().getHeader("aitNo", String.class);
                    Map<String, Object> rowData = exchange.getIn().getBody(Map.class);
                    dbUtil.deleteRow("AIT_SCAN_WINDOW", "ait_no", aitNo);
                    dbUtil.addRow("AIT_SCAN_WINDOW", rowData);
                });

        // 2. Select AIT_SCAN_WINDOW and compare the data against the given AIT_NUMBER
        from("direct:selectAndCompareAitScanWindow")
                .process(exchange -> {
                    String aitNo = exchange.getIn().getHeader("aitNo", String.class);
                    AitScanWindow aitScanWindow = dbUtil.selectRow("AIT_SCAN_WINDOW", "ait_no", aitNo, AitScanWindow.class);
                    // Add your comparison logic here
                    exchange.getIn().setBody(aitScanWindow);
                });

        // 3. Delete KAFKA_STAT for a given where condition
        from("direct:deleteKafkaStat")
                .process(exchange -> {
                    String condition = exchange.getIn().getHeader("condition", String.class);
                    dbUtil.executeQuery("DELETE FROM KAFKA_STAT WHERE " + condition);
                });

        // 4. Run a Java process in the background on a remote Linux server
        from("direct:runJavaProcess")
                .process(exchange -> {
                    String host = "remote-server";
                    String command = "java -jar /path/to/your/application.jar";
                    String result = sshUtil.executeCommand(host, command);
                    exchange.getIn().setBody(result);
                });

        // 5. Query KAFKA_STAT table
        from("direct:queryKafkaStat")
                .process(exchange -> {
                    String sql = "SELECT * FROM KAFKA_STAT";
                    exchange.getIn().setBody(dbUtil.queryForList(sql));
                });
    }
}
