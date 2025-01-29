package com.boa.tcautomation.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tc_steps")
public class TcSteps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;
    private String tcId;
    private String stepName;
    @Column(columnDefinition = "TEXT")
    private String parameters;
    private int sequenceNo;
    private String status;
}
