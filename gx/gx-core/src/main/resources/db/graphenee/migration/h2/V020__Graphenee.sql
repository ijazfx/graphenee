alter table gx_audit_log add column username varchar(50);
alter table gx_audit_log add column detail varchar(200);
alter table gx_audit_log add column remote_address varchar(40);