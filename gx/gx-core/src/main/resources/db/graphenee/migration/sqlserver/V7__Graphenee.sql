alter table gx_email_template add sms_body nvarchar(max) not null default '';
alter table gx_email_template add template_code nvarchar(100) not null default '';
