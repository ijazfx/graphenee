alter table gx_access_key add column record_version integer default 1;
alter table gx_access_key add column sort_order integer default 1;
alter table gx_access_key add column date_created timestamp;
alter table gx_access_key add column date_modified timestamp;
alter table gx_access_key add column created_by varchar(50);
alter table gx_access_key add column modified_by varchar(50);

alter table gx_access_log add column record_version integer default 1;
alter table gx_access_log add column sort_order integer default 1;
alter table gx_access_log add column date_created timestamp;
alter table gx_access_log add column date_modified timestamp;
alter table gx_access_log add column created_by varchar(50);
alter table gx_access_log add column modified_by varchar(50);

alter table gx_audit_log add column record_version integer default 1;
alter table gx_audit_log add column sort_order integer default 1;
alter table gx_audit_log add column date_created timestamp;
alter table gx_audit_log add column date_modified timestamp;
alter table gx_audit_log add column created_by varchar(50);
alter table gx_audit_log add column modified_by varchar(50);

alter table gx_city add column record_version integer default 1;
alter table gx_city add column sort_order integer default 1;
alter table gx_city add column date_created timestamp;
alter table gx_city add column date_modified timestamp;
alter table gx_city add column created_by varchar(50);
alter table gx_city add column modified_by varchar(50);

alter table gx_country add column record_version integer default 1;
alter table gx_country add column sort_order integer default 1;
alter table gx_country add column date_created timestamp;
alter table gx_country add column date_modified timestamp;
alter table gx_country add column created_by varchar(50);
alter table gx_country add column modified_by varchar(50);

alter table gx_currency add column record_version integer default 1;
alter table gx_currency add column sort_order integer default 1;
alter table gx_currency add column date_created timestamp;
alter table gx_currency add column date_modified timestamp;
alter table gx_currency add column created_by varchar(50);
alter table gx_currency add column modified_by varchar(50);

alter table gx_document add column record_version integer default 1;
-- alter table gx_document add column sort_order integer default 1;
alter table gx_document add column date_created timestamp;
alter table gx_document add column date_modified timestamp;
alter table gx_document add column created_by varchar(50);
alter table gx_document add column modified_by varchar(50);

alter table gx_document_type add column record_version integer default 1;
alter table gx_document_type add column sort_order integer default 1;
alter table gx_document_type add column date_created timestamp;
alter table gx_document_type add column date_modified timestamp;
alter table gx_document_type add column created_by varchar(50);
alter table gx_document_type add column modified_by varchar(50);

alter table gx_email_template add column record_version integer default 1;
alter table gx_email_template add column sort_order integer default 1;
alter table gx_email_template add column date_created timestamp;
alter table gx_email_template add column date_modified timestamp;
alter table gx_email_template add column created_by varchar(50);
alter table gx_email_template add column modified_by varchar(50);

alter table gx_file_tag add column record_version integer default 1;
alter table gx_file_tag add column sort_order integer default 1;
alter table gx_file_tag add column date_created timestamp;
alter table gx_file_tag add column date_modified timestamp;
alter table gx_file_tag add column created_by varchar(50);
alter table gx_file_tag add column modified_by varchar(50);

alter table gx_folder add column record_version integer default 1;
-- alter table gx_folder add column sort_order integer default 1;
alter table gx_folder add column date_created timestamp;
alter table gx_folder add column date_modified timestamp;
alter table gx_folder add column created_by varchar(50);
alter table gx_folder add column modified_by varchar(50);

alter table gx_gender add column record_version integer default 1;
alter table gx_gender add column sort_order integer default 1;
alter table gx_gender add column date_created timestamp;
alter table gx_gender add column date_modified timestamp;
alter table gx_gender add column created_by varchar(50);
alter table gx_gender add column modified_by varchar(50);

alter table gx_namespace add column record_version integer default 1;
alter table gx_namespace add column sort_order integer default 1;
alter table gx_namespace add column date_created timestamp;
alter table gx_namespace add column date_modified timestamp;
alter table gx_namespace add column created_by varchar(50);
alter table gx_namespace add column modified_by varchar(50);

alter table gx_namespace_property add column record_version integer default 1;
alter table gx_namespace_property add column sort_order integer default 1;
alter table gx_namespace_property add column date_created timestamp;
alter table gx_namespace_property add column date_modified timestamp;
alter table gx_namespace_property add column created_by varchar(50);
alter table gx_namespace_property add column modified_by varchar(50);

alter table gx_password_history add column record_version integer default 1;
alter table gx_password_history add column sort_order integer default 1;
alter table gx_password_history add column date_created timestamp;
alter table gx_password_history add column date_modified timestamp;
alter table gx_password_history add column created_by varchar(50);
alter table gx_password_history add column modified_by varchar(50);

alter table gx_password_policy add column record_version integer default 1;
alter table gx_password_policy add column sort_order integer default 1;
alter table gx_password_policy add column date_created timestamp;
alter table gx_password_policy add column date_modified timestamp;
alter table gx_password_policy add column created_by varchar(50);
alter table gx_password_policy add column modified_by varchar(50);

alter table gx_registered_device add column record_version integer default 1;
alter table gx_registered_device add column sort_order integer default 1;
alter table gx_registered_device add column date_created timestamp;
alter table gx_registered_device add column date_modified timestamp;
alter table gx_registered_device add column created_by varchar(50);
alter table gx_registered_device add column modified_by varchar(50);

alter table gx_resource add column record_version integer default 1;
alter table gx_resource add column sort_order integer default 1;
alter table gx_resource add column date_created timestamp;
alter table gx_resource add column date_modified timestamp;
alter table gx_resource add column created_by varchar(50);
alter table gx_resource add column modified_by varchar(50);

alter table gx_saved_query add column record_version integer default 1;
alter table gx_saved_query add column sort_order integer default 1;
alter table gx_saved_query add column date_created timestamp;
alter table gx_saved_query add column date_modified timestamp;
alter table gx_saved_query add column created_by varchar(50);
alter table gx_saved_query add column modified_by varchar(50);

alter table gx_security_group add column record_version integer default 1;
alter table gx_security_group add column sort_order integer default 1;
alter table gx_security_group add column date_created timestamp;
alter table gx_security_group add column date_modified timestamp;
alter table gx_security_group add column created_by varchar(50);
alter table gx_security_group add column modified_by varchar(50);

alter table gx_security_policy add column record_version integer default 1;
alter table gx_security_policy add column sort_order integer default 1;
alter table gx_security_policy add column date_created timestamp;
alter table gx_security_policy add column date_modified timestamp;
alter table gx_security_policy add column created_by varchar(50);
alter table gx_security_policy add column modified_by varchar(50);

alter table gx_security_policy_document add column record_version integer default 1;
alter table gx_security_policy_document add column sort_order integer default 1;
alter table gx_security_policy_document add column date_created timestamp;
alter table gx_security_policy_document add column date_modified timestamp;
alter table gx_security_policy_document add column created_by varchar(50);
alter table gx_security_policy_document add column modified_by varchar(50);

alter table gx_sms_provider add column record_version integer default 1;
alter table gx_sms_provider add column sort_order integer default 1;
alter table gx_sms_provider add column date_created timestamp;
alter table gx_sms_provider add column date_modified timestamp;
alter table gx_sms_provider add column created_by varchar(50);
alter table gx_sms_provider add column modified_by varchar(50);

alter table gx_state add column record_version integer default 1;
alter table gx_state add column sort_order integer default 1;
alter table gx_state add column date_created timestamp;
alter table gx_state add column date_modified timestamp;
alter table gx_state add column created_by varchar(50);
alter table gx_state add column modified_by varchar(50);

alter table gx_supported_locale add column record_version integer default 1;
alter table gx_supported_locale add column sort_order integer default 1;
alter table gx_supported_locale add column date_created timestamp;
alter table gx_supported_locale add column date_modified timestamp;
alter table gx_supported_locale add column created_by varchar(50);
alter table gx_supported_locale add column modified_by varchar(50);

alter table gx_term add column record_version integer default 1;
alter table gx_term add column sort_order integer default 1;
alter table gx_term add column date_created timestamp;
alter table gx_term add column date_modified timestamp;
alter table gx_term add column created_by varchar(50);
alter table gx_term add column modified_by varchar(50);

alter table gx_term_translation add column record_version integer default 1;
alter table gx_term_translation add column sort_order integer default 1;
alter table gx_term_translation add column date_created timestamp;
alter table gx_term_translation add column date_modified timestamp;
alter table gx_term_translation add column created_by varchar(50);
alter table gx_term_translation add column modified_by varchar(50);

alter table gx_user_account add column record_version integer default 1;
alter table gx_user_account add column sort_order integer default 1;
alter table gx_user_account add column date_created timestamp;
alter table gx_user_account add column date_modified timestamp;
alter table gx_user_account add column created_by varchar(50);
alter table gx_user_account add column modified_by varchar(50);
