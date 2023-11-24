CREATE TABLE gx_folder (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    folder_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    note VARCHAR(200),
    tags CLOB,
    oid_folder INT,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_folder) REFERENCES gx_folder(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_folder_audit_log_join (
    oid_folder INT NOT NULL,
    oid_audit_log INT NOT NULL,
    FOREIGN KEY (oid_folder) REFERENCES gx_folder(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_audit_log) REFERENCES gx_audit_log(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_document_type (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    note VARCHAR(200),
    tags CLOB,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_document (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    document_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    note VARCHAR(200),
    mime_type VARCHAR(100),
    size BIGINT NOT NULL DEFAULT 0,
    version_no INT NOT NULL DEFAULT 0,
    path VARCHAR(1000) NOT NULL,
    tags CLOB,
    oid_document INT,
    oid_folder INT,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_document) REFERENCES gx_document(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_folder) REFERENCES gx_folder(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_document_audit_log_join (
    oid_document INT NOT NULL,
    oid_audit_log INT NOT NULL,
    FOREIGN KEY (oid_document) REFERENCES gx_document(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_audit_log) REFERENCES gx_audit_log(oid) ON DELETE CASCADE ON UPDATE CASCADE
);
