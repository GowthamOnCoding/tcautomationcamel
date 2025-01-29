// StepConfig.java
package com.boa.tcautomation.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "STEP_CONFIG")
public class StepConfig {
    @Id
    private String stepName;
    private String description;
    private Integer timeoutSeconds;
    private Integer maxRetries;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "stepName", fetch = FetchType.EAGER)
    @OrderBy("sequenceNo")
    private List<StepSchemaMapping> schemaMappings;
}
