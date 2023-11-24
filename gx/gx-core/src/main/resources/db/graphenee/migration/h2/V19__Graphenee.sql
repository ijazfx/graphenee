-- Alter table gx_document
ALTER TABLE gx_document ADD COLUMN sort_order INT NOT NULL DEFAULT 0;
ALTER TABLE gx_document ADD COLUMN issue_date TIMESTAMP;
ALTER TABLE gx_document ADD COLUMN expiry_date TIMESTAMP;
ALTER TABLE gx_document ADD COLUMN expiry_reminder_in_days INT;

-- Alter table gx_folder
ALTER TABLE gx_folder ADD COLUMN sort_order INT NOT NULL DEFAULT 0;
ALTER TABLE gx_folder ADD COLUMN expiry_reminder_in_days INT;
