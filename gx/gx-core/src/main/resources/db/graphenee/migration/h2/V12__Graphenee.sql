CREATE TABLE gx_sms_provider (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    provider_name VARCHAR(50) NOT NULL,
    implementation_class VARCHAR(200) NOT NULL,
    config_data BLOB,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);
