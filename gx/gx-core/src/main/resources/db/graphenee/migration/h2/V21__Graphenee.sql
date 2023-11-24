-- Alter table gx_user_account
ALTER TABLE gx_user_account ADD COLUMN preferences CLOB NOT NULL DEFAULT '{}';
