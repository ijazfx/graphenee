CREATE TABLE gx_password_policy (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    oid_namespace INT NOT NULL,
    password_policy_name VARCHAR(50),
    max_history INT NOT NULL DEFAULT 6,
    max_age INT NOT NULL DEFAULT 60,
    min_length INT NOT NULL DEFAULT 8,
    is_user_username_allowed BOOLEAN NOT NULL DEFAULT FALSE,
    max_allowed_matching_user_name INT NOT NULL DEFAULT 2,
    min_uppercase INT NOT NULL DEFAULT 1,
    min_lowercase INT NOT NULL DEFAULT 1,
    min_numbers INT NOT NULL DEFAULT 1,
    min_special_charaters INT NOT NULL DEFAULT 1,
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE gx_password_history (
    oid INT AUTO_INCREMENT PRIMARY KEY,
    oid_user_account INT NOT NULL,
    hashed_password VARCHAR(200) NOT NULL,
    password_date TIMESTAMP NOT NULL,
    FOREIGN KEY (oid_user_account) REFERENCES gx_user_account(oid) ON DELETE CASCADE ON UPDATE CASCADE
);
