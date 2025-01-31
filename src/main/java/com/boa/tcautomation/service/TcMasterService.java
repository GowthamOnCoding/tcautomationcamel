package com.boa.tcautomation.service;

import com.boa.tcautomation.model.AitDbProp;
import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.QueryConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

@Service
public class TcMasterService {

    @Autowired
    private DbUtil dbUtil;

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

    public void deleteInsertScanWindow(TcMaster tcMaster) {
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

    public void processTestCase(TcMaster tcMaster) {
        log.info("Processing test case: " + tcMaster.getTcId());
        List<TcSteps> tcSteps = getTcStepsByTcId(tcMaster.getTcId());
        for (TcSteps step : tcSteps) {
            String methodName = step.getStepName();
            try {
                Method method = this.getClass().getMethod(methodName, TcMaster.class);
                method.invoke(this, tcMaster);
            } catch (Exception e) {
                log.severe("Error invoking method " + methodName + ": " + e.getMessage());
            }
        }
    }

    public void runLinuxCommand(TcMaster tcMaster) {
        log.info("Running Linux command for test case: " + tcMaster.getTcId());
        // Implementation
    }
}
