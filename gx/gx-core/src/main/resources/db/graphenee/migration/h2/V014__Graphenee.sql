CREATE TABLE gx_mobile_application (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    application_name VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_registered_device (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    oid_mobile_application INT NOT NULL,
    system_name VARCHAR(50),
    unique_id VARCHAR(100),
    is_tablet BOOLEAN NOT NULL DEFAULT FALSE,
    brand VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    owner_id VARCHAR(100),
    FOREIGN KEY (oid_mobile_application) REFERENCES gx_mobile_application(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX owner_id_index ON gx_registered_device(owner_id);
