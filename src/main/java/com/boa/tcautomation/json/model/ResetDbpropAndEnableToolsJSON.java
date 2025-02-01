package com.boa.tcautomation.json.model;

import lombok.Data;

@Data
public class ResetDbpropAndEnableToolsJSON {
    private String aitNumber;
    private String configId;
    private String enableTools;
    private String expectedOutput;
}
