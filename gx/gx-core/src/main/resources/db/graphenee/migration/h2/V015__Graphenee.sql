DROP TABLE IF EXISTS gx_registered_device;
DROP TABLE IF EXISTS gx_mobile_application;

CREATE TABLE gx_registered_device (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    system_name VARCHAR(50),
    device_token VARCHAR(200),
    is_tablet BOOLEAN NOT NULL DEFAULT FALSE,
    brand VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    owner_id VARCHAR(100),
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS owner_id_index ON gx_registered_device(owner_id);
