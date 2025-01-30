package com.boa.tcautomation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tc_master")
public class TcMaster {
    @Id
    private String tcId;
    private String tcName;
    private String description;
    private String flag;
    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private String is_active;
    private String ait_no;
}
