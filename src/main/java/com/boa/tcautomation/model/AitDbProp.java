package com.boa.tcautomation.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ait_dbprop")
public class AitDbProp {

    @Id
    private String aitNo;
    private String id;
    private String profile;
    private String dbType;
    private String machineName;
    private String dbName;
    private String schemaName;
    private String userId;
    private String password;
    private String topicName;
    private String jdbcUrl;
    private Integer noOfConnection;
    private String emailId;
    private boolean isActive;
    private String lastUpdatedUser;
    private boolean sddActive;
    private boolean aimlIsActive;
    private String tableList;
    private String execStatus;
    private String aitCadence;
    private boolean idwSdd;
    private boolean idwUdd;
    private boolean iedpsSdd;
    private String reportTopicName;
    private boolean funnelUdd;
    private boolean funnelSdd;
    private boolean aimlSdd;
    private boolean aimlUdd;
    private String funnelDestination;
    private boolean funnelDiscovery;
    private boolean aimlDiscovery;
    private boolean idwDiscovery;
    private boolean iedpsDiscovery;
    private boolean aimlValidation;
    private String fftDestination;
    private Integer aitNum;
    private String jdbcConStr;
    private String lob;
    private String environment;

    // Getters and Setters
    // ...
}
