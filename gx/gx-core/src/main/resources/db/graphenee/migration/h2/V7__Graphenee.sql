-- Alter table gx_email_template
ALTER TABLE gx_email_template ADD COLUMN sms_body CLOB NOT NULL DEFAULT '';
ALTER TABLE gx_email_template ADD COLUMN template_code VARCHAR(100) NOT NULL DEFAULT '';
