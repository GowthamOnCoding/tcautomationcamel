package com.boa.tcautomation.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "step_config")
public class StepConfig {

    @Id
    @Column(nullable = false)
    private String stepName;

    @Column
    private String description;

    @Column(nullable = false)
    private int timeoutSeconds;

    @Column(nullable = false)
    private int maxRetries;

    @Column
    private String parameterSchema;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdDate;

    @Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp modifiedDate;
}
