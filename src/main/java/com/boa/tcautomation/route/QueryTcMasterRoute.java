package com.boa.tcautomation.route;
import com.boa.tcautomation.model.AitDbProp;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.QueryConstants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * RouteBuilder implementation to query active TcMaster records and process each test case.
 */
@Component
public class QueryTcMasterRoute extends RouteBuilder {

    @Autowired
    private DbUtil dbUtil;

    /**
     * Configures the routes for querying TcMaster records and processing each test case.
     */
    @Override
    public void configure() {

        // Route to query active TcMaster records
        from("direct:queryTcMaster")
                .process(exchange -> {
                    // SQL query to select active TcMaster records
                    String sql = QueryConstants.SELECT_ACTIVE_TC_MASTERS;
                    // Execute the query and map the results to a list of TcMaster objects
                    List<TcMaster> activeTcMasters = dbUtil.queryForListWithMapping(sql, TcMaster.class);

                    // Create an ExecutorService to run tasks in virtual threads
                    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
                    // Submit a task for each TcMaster to process the test case
                for (TcMaster tcMaster : activeTcMasters) {
                        executor.submit(() -> {
                            // Send the TcMaster object to the direct:processTestCase route
                            getContext().createProducerTemplate().sendBody("direct:processTestCase", tcMaster);
                        });
                    }
                    // Shutdown the executor service
                    executor.shutdown();

                    // Set the list of active TcMasters as the body of the exchange
                    exchange.getIn().setBody(activeTcMasters);
                })
                .log("Active TcMasters: ${body}");

        // Route to process each test case
        from("direct:processTestCase")
                .process(exchange -> {
                    // Get the TcMaster object from the exchange body
                    TcMaster tcMaster = exchange.getIn().getBody(TcMaster.class);
                    log.info("Processing test case: " + tcMaster.getTcId());
                    String aitNo = tcMaster.getAit_no();
                    if(aitNo.startsWith("AIT_")) {
                        aitNo=aitNo.replace("AIT_", "");
                        log.info("Processing AIT test case: " + tcMaster.getTcId());
                    } else {
                        log.info("Processing non-AIT test case: " + tcMaster.getTcId());
                    }
                    String sql = QueryConstants.SELECT_AIT_DB_PROPS.replace("<AIT_NO>", aitNo);
                    List<AitDbProp> aitDbProps = dbUtil.queryForListWithMapping(sql, AitDbProp.class);
                    for(AitDbProp aitDbProp : aitDbProps) {
                        log.info("AIT DB Prop: " + aitDbProp);
                        // Step 1: Delete existing records in ait_scan_window table
                        String deleteSql = QueryConstants.DELETE_AIT_SCAN_WINDOW
                                .replace("<AIT_NO>", "AIT_"+aitDbProp.getAitNo())
                                .replace("<DB_TYPE>", aitDbProp.getDbType());
                        log.info("Executing delete SQL: " + deleteSql);
                        boolean deleteSuccess = dbUtil.executeQuery(deleteSql);
                        log.info("Delete operation success: " + deleteSuccess);

                        // Step 2: Insert new records into ait_scan_window table
                        String insertSql = QueryConstants.INSERT_AIT_SCAN_WINDOW
                                .replace("<AIT_NO>", "AIT_"+aitDbProp.getAitNo())
                                .replace("<DB_TYPE>", aitDbProp.getDbType())
                                .replace("<PROFILE>", aitDbProp.getProfile());
                        log.info("Executing insert SQL: " + insertSql);
                        boolean insertSuccess = dbUtil.executeQuery(insertSql);
                        log.info("Insert operation success: " + insertSuccess);
                    }
                    // Continue with other steps to process the test case
                    // ...
                });

        // Run once timer to call the queryTcMaster endpoint
        from("timer://runOnce?repeatCount=1&delay=1000")
                .to("direct:queryTcMaster");
    }
}
