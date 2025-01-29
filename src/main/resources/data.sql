
-- Insert sample schemas
INSERT INTO PARAMETER_SCHEMA (SCHEMA_ID, SCHEMA_VERSION, SCHEMA_DEFINITION, DESCRIPTION) VALUES
('BASE_PARAMETERS', 1, '{
    "type": "object",
    "required": ["environment", "region"],
    "properties": {
        "environment": {
            "type": "string",
            "enum": ["dev", "test", "prod"]
        },
        "region": {
            "type": "string",
            "pattern": "^[A-Z]{2}-[A-Z]+-\\d+$"
        }
    }
}', 'Base parameters required for all steps');

INSERT INTO PARAMETER_SCHEMA (SCHEMA_ID, SCHEMA_VERSION, SCHEMA_DEFINITION, DESCRIPTION) VALUES
('AIT_PARAMETERS', 1, '{
    "type": "object",
    "required": ["aitNumber"],
    "properties": {
        "aitNumber": {
            "type": "string",
            "pattern": "^AIT\\d{6}$"
        }
    }
}', 'AIT specific parameters');

INSERT INTO PARAMETER_SCHEMA (SCHEMA_ID, SCHEMA_VERSION, SCHEMA_DEFINITION, DESCRIPTION) VALUES
('JAVA_PROCESS_PARAMETERS', 1, '{
    "type": "object",
    "required": ["processName", "server"],
    "properties": {
        "processName": {
            "type": "string",
            "minLength": 1
        },
        "server": {
            "type": "string",
            "format": "hostname"
        },
        "timeout": {
            "type": "integer",
            "minimum": 0,
            "maximum": 3600
        }
    }
}', 'Java process execution parameters');

-- Insert step configurations
INSERT INTO STEP_CONFIG (STEP_NAME, DESCRIPTION, TIMEOUT_SECONDS, MAX_RETRIES) VALUES
('DELETE_INSERT_AIT_SCAN_WINDOW', 'Deletes and inserts AIT scan window records', 300, 3);

INSERT INTO STEP_CONFIG (STEP_NAME, DESCRIPTION, TIMEOUT_SECONDS, MAX_RETRIES) VALUES
('INVOKE_JAVA_PROCESS', 'Invokes a Java process on a remote server', 600, 2);

-- Insert schema mappings
INSERT INTO STEP_SCHEMA_MAPPING (STEP_NAME, SCHEMA_ID, IS_REQUIRED, SEQUENCE_NO) VALUES
('DELETE_INSERT_AIT_SCAN_WINDOW', 'BASE_PARAMETERS', true, 1),
('DELETE_INSERT_AIT_SCAN_WINDOW', 'AIT_PARAMETERS', true, 2),
('INVOKE_JAVA_PROCESS', 'BASE_PARAMETERS', true, 1),
('INVOKE_JAVA_PROCESS', 'JAVA_PROCESS_PARAMETERS', true, 2);

-- Insert sample test case
INSERT INTO TC_MASTER (TC_ID, TC_NAME, FLAG, DESCRIPTION) VALUES
('TC001', 'AIT Scan Window Update', 'ENABLED', 'Updates AIT scan window data');

INSERT INTO TC_STEPS (STEP_ID, TC_ID, STEP_NAME, PARAMETERS, SEQUENCE_NO) VALUES
(1, 'TC001', 'DELETE_INSERT_AIT_SCAN_WINDOW', '{
    "environment": "prod",
    "region": "US-EAST-1",
    "aitNumber": "AIT123456"
}', 1);

INSERT INTO TC_STEPS (STEP_ID, TC_ID, STEP_NAME, PARAMETERS, SEQUENCE_NO) VALUES
(2, 'TC001', 'INVOKE_JAVA_PROCESS', '{
    "environment": "prod",
    "region": "US-EAST-1",
    "processName": "DataCleanup",
    "server": "app-server-1.example.com",
    "timeout": 300
}', 2);
