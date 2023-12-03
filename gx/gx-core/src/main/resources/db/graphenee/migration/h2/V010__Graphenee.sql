-- gx_access_key table
CREATE TABLE gx_access_key (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    access_key UUID NOT NULL,
    secret VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    access_key_type INT
);

-- gx_user_account_access_key_join table
CREATE TABLE gx_user_account_access_key_join (
    oid_user_account INT NOT NULL,
    oid_access_key INT NOT NULL,
    FOREIGN KEY (oid_user_account) REFERENCES gx_user_account(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_access_key) REFERENCES gx_access_key(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- gx_access_key_security_group_join table
CREATE TABLE gx_access_key_security_group_join (
    oid_access_key INT NOT NULL,
    oid_security_group INT NOT NULL,
    FOREIGN KEY (oid_security_group) REFERENCES gx_security_group(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_access_key) REFERENCES gx_access_key(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- gx_access_key_security_policy_join table
CREATE TABLE gx_access_key_security_policy_join (
    oid_access_key INT NOT NULL,
    oid_security_policy INT NOT NULL,
    FOREIGN KEY (oid_security_policy) REFERENCES gx_security_policy(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_access_key) REFERENCES gx_access_key(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- gx_resource table
CREATE TABLE gx_resource (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    resource_name VARCHAR(50),
    resource_description VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- gx_access_log table
CREATE TABLE gx_access_log (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    oid_access_key INT NOT NULL,
    oid_resource INT NOT NULL,
    access_time TIMESTAMP NOT NULL,
    is_success BOOLEAN NOT NULL DEFAULT FALSE,
    access_type INT NOT NULL,
    FOREIGN KEY (oid_access_key) REFERENCES gx_access_key(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_resource) REFERENCES gx_resource(oid) ON DELETE CASCADE ON UPDATE CASCADE
);
