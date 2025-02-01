package com.boa.tcautomation.json.model;

import lombok.Data;

@Data
public class LinuxCommandJSON {
    private String hostname;
    private String process;
    private String reqType;
    private String command;
    private String bgFlag;
    private String expectedOutput;
}
