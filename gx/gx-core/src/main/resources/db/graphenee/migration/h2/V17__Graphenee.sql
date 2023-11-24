-- Add column oid_namespace to gx_user_account
ALTER TABLE gx_user_account ADD COLUMN oid_namespace INT;

-- Add foreign key constraint gx_user_account_namespace_fkey
ALTER TABLE gx_user_account ADD CONSTRAINT gx_user_account_namespace_fkey
    FOREIGN KEY (oid_namespace) REFERENCES gx_namespace(oid) ON DELETE SET NULL ON UPDATE CASCADE;
