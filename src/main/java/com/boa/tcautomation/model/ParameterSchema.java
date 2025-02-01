package com.boa.tcautomation.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PARAMETER_SCHEMA")
public class ParameterSchema {
    @Id
    private String schemaId;
    private Integer schemaVersion;
    @Column(columnDefinition = "TEXT")
    private String schemaDefinition;
    private boolean isActive;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    // Getter and Setter for isActive
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
