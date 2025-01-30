package com.boa.tcautomation.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DbUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to add a row to a table
    public void addRow(String tableName, Map<String, Object> rowData) {
        String columns = String.join(", ", rowData.keySet());
        String values = String.join(", ", rowData.values().stream().map(value -> "'" + value + "'").toArray(String[]::new));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        jdbcTemplate.execute(sql);
    }

    // Method to delete a row from a table
    public void deleteRow(String tableName, String columnName, Object value) {
        String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    // Method to update a row in a table
    public void updateRow(String tableName, String columnName, Object value, Map<String, Object> updatedData) {
        String setClause = String.join(", ", updatedData.entrySet().stream()
                .map(entry -> entry.getKey() + " = '" + entry.getValue() + "'")
                .toArray(String[]::new));
        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    // Method to clone a row in a table
    public void cloneRow(String tableName, String columnName, Object value) {
        String sql = "INSERT INTO " + tableName + " SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        jdbcTemplate.execute(sql);
    }

    // Method to insert a row into a table
    public void insertRow(String tableName, Map<String, Object> rowData) {
        addRow(tableName, rowData);
    }

    // Method to select a row from a table
    public <T> T selectRow(String tableName, String columnName, Object value, Class<T> mappingClass) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{value}, new BeanPropertyRowMapper<>(mappingClass));
    }

    // Method to execute a custom query
    public void executeQuery(String sql) {
        jdbcTemplate.execute(sql);
    }

    // Method to query for a list of rows
    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    // Method to query for a single object
    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return jdbcTemplate.queryForObject(sql, requiredType);
    }
}
