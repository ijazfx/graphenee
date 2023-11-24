CREATE TABLE gx_namespace (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    namespace VARCHAR(100) NOT NULL,
    namespace_description VARCHAR(100),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE gx_namespace_property (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    property_key VARCHAR(100) NOT NULL,
    property_value VARCHAR(500) NOT NULL,
    property_default_value VARCHAR(500) NOT NULL,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO gx_namespace (namespace, namespace_description, is_protected) VALUES ('com.graphenee.system', 'To be used by Graphenee', TRUE);

CREATE TABLE gx_supported_locale (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    locale_name VARCHAR(50) NOT NULL,
    locale_code VARCHAR(10) NOT NULL,
    is_left_to_right BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO gx_supported_locale (locale_name, locale_code, is_protected) VALUES ('English (United States)', 'en_US', TRUE);

CREATE TABLE gx_term (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    term_key VARCHAR(100) NOT NULL,
    term_singular VARCHAR(1000) NOT NULL,
    term_plural VARCHAR(1000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE,
    oid_supported_locale INT NOT NULL,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_supported_locale) REFERENCES gx_supported_locale(oid) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_email_template (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    template_name VARCHAR(50) NOT NULL,
    subject VARCHAR(500) NOT NULL,
    body CLOB NOT NULL,
    cc_list VARCHAR(500),
    bcc_list VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_security_group (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    security_group_name VARCHAR(50) NOT NULL,
    priority INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_security_policy (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    security_policy_name VARCHAR(50) NOT NULL,
    priority INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE,
    oid_namespace INT NOT NULL,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_security_policy_document (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    document_json CLOB NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT TRUE,
    oid_security_policy INT NOT NULL,
    FOREIGN KEY (oid_security_policy) REFERENCES gx_security_policy(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE gx_security_group_security_policy_join (
    oid_security_group INT NOT NULL,
    oid_security_policy INT NOT NULL,
    FOREIGN KEY (oid_security_group) REFERENCES gx_security_group(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_security_policy) REFERENCES gx_security_policy(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_gender (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    gender_name VARCHAR(10) NOT NULL,
    gender_code VARCHAR(3) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO gx_gender (gender_name, gender_code, is_protected) VALUES ('Male', 'M', TRUE);
INSERT INTO gx_gender (gender_name, gender_code, is_protected) VALUES ('Female', 'F', TRUE);

CREATE TABLE gx_user_account (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(200) NOT NULL,
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    full_name_native VARCHAR(100),
    email VARCHAR(200),
    profile_image BLOB,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    is_protected BOOLEAN NOT NULL DEFAULT FALSE,
    is_password_change_required BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(100),
    verification_token_expiry_date TIMESTAMP,
    account_activation_date TIMESTAMP,
    last_login_date TIMESTAMP,
    last_login_failed_date TIMESTAMP,
    count_login_failed INT NOT NULL DEFAULT 0,
    oid_gender INT,
    FOREIGN KEY (oid_gender) REFERENCES gx_gender(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO gx_user_account (username, password, is_password_change_required, is_protected) VALUES ('admin', 'change_on_install', TRUE, TRUE);

CREATE TABLE gx_user_account_security_group_join (
    oid_user_account INT NOT NULL,
    oid_security_group INT NOT NULL,
    FOREIGN KEY (oid_user_account) REFERENCES gx_user_account(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_security_group) REFERENCES gx_security_group(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_user_account_security_policy_join (
    oid_user_account INT NOT NULL,
    oid_security_policy INT NOT NULL,
    FOREIGN KEY (oid_user_account) REFERENCES gx_user_account(oid) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (oid_security_policy) REFERENCES gx_security_policy(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_audit_log (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    audit_date TIMESTAMP NOT NULL,
    audit_event VARCHAR(50) NOT NULL,
    audit_entity VARCHAR(50),
    oid_audit_entity INT,
    oid_user_account INT,
    FOREIGN KEY (oid_user_account) REFERENCES gx_user_account(oid) ON DELETE RESTRICT ON UPDATE CASCADE
);