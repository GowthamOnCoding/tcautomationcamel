package com.boa.tcautomation.service;

import com.boa.tcautomation.json.model.LinuxCommandJSON;
import com.boa.tcautomation.model.*;
import com.boa.tcautomation.util.DatabaseToCsvUtil;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.QueryConstants;
import com.boa.tcautomation.util.SshUtil;
import com.boa.tcautomation.validator.ParameterValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class TcMasterService {

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private SshUtil sshUtil;

    @Autowired
    private DatabaseToCsvUtil databaseToCsvUtil;

    @Autowired
    private ParameterValidationService parameterValidationService;

    private static final Logger log = Logger.getLogger(TcMasterService.class.getName());

    public List<AitDbProp> queryAitDbProps(String aitNo) {
        String sql = QueryConstants.SELECT_AIT_DB_PROPS.replace("<AIT_NO>", aitNo);
        log.info("Executing queryAitDbProps SQL: " + sql);
        return dbUtil.queryForListWithMapping(sql, AitDbProp.class);
    }

    public boolean deleteAitScanWindow(String aitNo, String dbType) {
        String deleteSql = QueryConstants.DELETE_AIT_SCAN_WINDOW
                .replace("<AIT_NO>", aitNo)
                .replace("<DB_TYPE>", dbType);
        log.info("Executing deleteAitScanWindow SQL: " + deleteSql);
        return dbUtil.executeQuery(deleteSql);
    }

    public boolean insertAitScanWindow(AitDbProp aitDbProp) {
        String insertSql = QueryConstants.INSERT_AIT_SCAN_WINDOW
                .replace("<AIT_NO>", "AIT_" + aitDbProp.getAitNo())
                .replace("<DB_TYPE>", aitDbProp.getDbType())
                .replace("<PROFILE>", aitDbProp.getProfile());
        log.info("Executing insertAitScanWindow SQL: " + insertSql);
        return dbUtil.executeQuery(insertSql);
    }

    public void deleteInsertScanWindow(TcMaster tcMaster,TcSteps tcStep) {
        log.info("Processing test case: " + tcMaster.getTcId());
        String aitNo = tcMaster.getAit_no();
        if (aitNo.startsWith("AIT_")) {
            aitNo = aitNo.replace("AIT_", "");
            log.info("Processing AIT test case: " + tcMaster.getTcId());
        } else {
            log.info("Processing non-AIT test case: " + tcMaster.getTcId());
        }
        List<AitDbProp> aitDbProps = queryAitDbProps(aitNo);
        for (AitDbProp aitDbProp : aitDbProps) {
            log.info("AIT DB Prop: " + aitDbProp);
            // Step 1: Delete existing records in ait_scan_window table
            boolean deleteSuccess = deleteAitScanWindow("AIT_" + aitDbProp.getAitNo(), aitDbProp.getDbType());
            log.info("Delete operation success: " + deleteSuccess);

            // Step 2: Insert new records into ait_scan_window table
            boolean insertSuccess = insertAitScanWindow(aitDbProp);
            log.info("Insert operation success: " + insertSuccess);
        }
    }

    public List<TcSteps> getTcStepsByTcId(String tcId) {
        String sql = QueryConstants.SELECT_TC_STEPS_BY_TC_ID.replace("<TC_ID>", tcId);
        log.info("Executing getTcStepsByTcId SQL: " + sql);
        return dbUtil.queryForListWithMapping(sql, TcSteps.class);
    }

    public ParameterSchema getParameterSchema(String schemaId) {
        String sql = QueryConstants.SELECT_PARAMETER_SCHEMA.replace("<SCHEMA_ID>", schemaId);
        log.info("Executing getParameterSchema SQL: " + sql);
        return dbUtil.queryForObject(sql, ParameterSchema.class);
    }

    public void processTestCase(TcMaster tcMaster) {
        log.info("Processing test case: " + tcMaster.getTcId());
        List<TcSteps> tcSteps = getTcStepsByTcId(tcMaster.getTcId());
        for (TcSteps step : tcSteps) {
            String methodName = step.getStepName();
            try {
                Method method = this.getClass().getMethod(methodName, TcMaster.class, TcSteps.class);
                method.invoke(this, tcMaster, step);
            } catch (Exception e) {
                log.severe("Error invoking method " + methodName + ": " + e.getMessage());
            }
        }
    }

    public void runLinuxCommand(TcMaster tcMaster, TcSteps step) {
        log.info("Running Linux command for test case: " + tcMaster.getTcId());
        try {
            if (getAndValidateParametersSchema(step)) {
                LinuxCommandJSON linuxCommandJSON = new ObjectMapper().readValue(step.getParameters(), LinuxCommandJSON.class);
                // Execute the command using SshUtil
                String output = sshUtil.executeCommand(linuxCommandJSON.getHostname(), linuxCommandJSON.getCommand());
                log.info("Command output: " + output);
            } else {
                log.severe("Failed to get and validate StepConfig");
            }
        } catch (Exception e) {
            log.severe("Error running command: " + e.getMessage());
        }
    }

    public void exportTableToCsv(TcMaster tcMaster, TcSteps step) {
        log.info("Exporting table to CSV for test case: " + tcMaster.getTcId());
        String tableName = step.getParameters(); // Assuming parameters column contains the table name
        String query = "SELECT * FROM " + tableName; // Use the provided table name
        String destination = "/path/to/destination"; // Replace with actual destination
        String fileName = "output.csv"; // Replace with actual file name
        try {
            databaseToCsvUtil.queryToCsv(query, destination, fileName);
        } catch (Exception e) {
            log.severe("Error exporting table to CSV: " + e.getMessage());
        }
    }
    private boolean getAndValidateParametersSchema(TcSteps step) {
        try {
            // Get the parameters and store them in a variable
            String parameters = step.getParameters();
            log.info("Parameters: " + parameters);

            // Run the query to get StepConfig
            String sql = QueryConstants.SELECT_STEP_CONFIG_BY_STEP_NAME.replace("<STEP_NAME>", step.getStepName());
            StepConfig stepConfig = dbUtil.queryForObject(sql, StepConfig.class);
            log.info("StepConfig: " + stepConfig);

            // If parameter schema ID is not null or empty, validate the parameters
            if (stepConfig.getParameterSchema() != null && !stepConfig.getParameterSchema().isEmpty()) {
                ParameterSchema parameterSchema = getParameterSchema(stepConfig.getParameterSchema());
                Map<String, String> parametersMap = new ObjectMapper().readValue(parameters, Map.class);
                parameterValidationService.validateParameters(step.getStepName(), parametersMap);
            }

            return true;
        } catch (Exception e) {
            log.severe("Error getting and validating StepConfig: " + e.getMessage());
            return false;
        }
    }
}
