package com.boa.tcautomation.util;

public class QueryConstants {
    public static final String SELECT_ACTIVE_TC_MASTERS = "SELECT * FROM testdb.tc_master WHERE is_active = 1";
    public static final String SELECT_AIT_DB_PROPS = "SELECT * FROM AIT_DBPROP where AIT_NO='<AIT_NO>'";
    public static final String DELETE_AIT_SCAN_WINDOW = "DELETE FROM ait_scan_window WHERE ait_no = '<AIT_NO>' AND db_type = '<DB_TYPE>'";
    public static final String INSERT_AIT_SCAN_WINDOW = "INSERT INTO ait_scan_window (ait_no, db_type, scan_day, start_time_est, end_time_est, last_updated, last_updated_user, profile) VALUES " +
            "('<AIT_NO>', '<DB_TYPE>', 'MON', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'TUE', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'WED', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'THU', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'FRI', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'SAT', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')," +
            "('<AIT_NO>', '<DB_TYPE>', 'SUN', '00:00', '23:59', CURRENT_TIMESTAMP, 'system', 'public')";
}
