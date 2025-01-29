// ValidationError.java
package com.boa.tcautomation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
    private String schemaId;
    private Object errorDetails;
}
