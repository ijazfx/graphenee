alter table gx_document add column sort_order integer not null default 0;
alter table gx_document add column issue_date timestamp;
alter table gx_document add column expiry_date timestamp;
alter table gx_document add column expiry_reminder_in_days integer;

alter table gx_folder add column sort_order integer not null default 0;
alter table gx_folder add column expiry_reminder_in_days integer;