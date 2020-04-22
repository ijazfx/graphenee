alter table gx_email_template add column sms_body text not null default '';
alter table gx_email_template add column template_code varchar(100) not null default '';
