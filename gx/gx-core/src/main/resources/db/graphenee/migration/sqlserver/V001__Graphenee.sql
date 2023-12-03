create table gx_namespace (
	oid integer primary key not null identity(1,1),
	namespace nvarchar(100) not null,
	namespace_description nvarchar(100),
	is_active bit not null default 1,
	is_protected bit not null default 0
);

create table gx_namespace_property (
	oid integer primary key not null identity(1,1),
	property_key nvarchar(100) not null,
	property_value nvarchar(500) not null,
	property_default_value nvarchar(500) not null,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete no action
);

insert into gx_namespace(namespace, namespace_description, is_protected) values ('com.graphenee.system', 'To be used by Graphenee', 1);

create table gx_supported_locale (
	oid integer primary key not null identity(1,1),
	locale_name nvarchar(50) not null,
	locale_code nvarchar(10) not null,
	is_left_to_right bit not null default 1,
	is_active bit not null default 1,
	is_protected bit not null default 0
);

insert into gx_supported_locale(locale_name, locale_code, is_protected) values ('English (United States)', 'en_US', 1);

create table gx_term (
	oid integer primary key not null identity(1,1),
	term_key nvarchar(100) not null,
	term_singular nvarchar(1000) not null,
	term_plural nvarchar(1000),
	is_active bit not null default 1,
	is_protected bit not null default 0,
	oid_supported_locale integer not null,
	oid_namespace integer not null,
	foreign key (oid_supported_locale) references gx_supported_locale(oid) on delete no action,
	foreign key (oid_namespace) references gx_namespace(oid) on delete no action
);

create table gx_email_template (
	oid integer primary key not null identity(1,1),
	template_name nvarchar(50) not null,
	subject nvarchar(500) not null,
	body nvarchar(max) not null,
	cc_list nvarchar(500),
	bcc_list nvarchar(500),
	is_active bit not null default 1,
	is_protected bit not null default 0,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete no action
);

create table gx_security_group (
	oid integer primary key not null identity(1,1),
	security_group_name nvarchar(50) not null,
	priority integer not null default 0,
	is_active bit not null default 1,
	is_protected bit not null default 0,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete no action
);

create table gx_security_policy (
	oid integer primary key not null identity(1,1),
	security_policy_name nvarchar(50) not null,
	priority integer not null default 0,
	is_active bit not null default 1,
	is_protected bit not null default 0,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete no action
);

create table gx_security_policy_document (
	oid integer primary key not null identity(1,1),
	document_json nvarchar(max) not null,
	is_default bit not null default 1,
	oid_security_policy integer not null,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete no action
);

create table gx_security_group_security_policy_join (
	oid_security_group integer not null,
	oid_security_policy integer not null,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete cascade
);

create table gx_gender(
	oid integer primary key not null identity(1,1),
	gender_name nvarchar(10) not null,
	gender_code nvarchar(3) not null,
	is_active bit not null default 1,
	is_protected bit not null default 0
);

insert into gx_gender(gender_name, gender_code, is_protected) values ('Male', 'M', 1);
insert into gx_gender(gender_name, gender_code, is_protected) values ('Female', 'F', 1);

create table gx_user_account (
	oid integer primary key not null identity(1,1),
	username nvarchar(50) not null,
	password nvarchar(200) not null,
	first_name nvarchar(30),
	last_name nvarchar(30),
	full_name_native nvarchar(100),
	email nvarchar(200),
	profile_image varbinary(max),
	is_active bit not null default 1,
	is_locked bit not null default 0,
	is_protected bit not null default 0,
	is_password_change_required bit default 0,
	verification_token nvarchar(100),
	verification_token_expiry_date datetime,
	account_activation_date datetime,
	last_login_date datetime,
	last_login_failed_date datetime,
	count_login_failed integer not null default 0,
	oid_gender integer,
	foreign key (oid_gender) references gx_gender(oid) on delete no action
);

insert into gx_user_account (username, password, is_password_change_required, is_protected) values ('admin', 'change_on_install', 1, 1);

create table gx_user_account_security_group_join (
	oid_user_account integer not null,
	oid_security_group integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade
);

create table gx_user_account_security_policy_join (
	oid_user_account integer not null,
	oid_security_policy integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete cascade
);

create table gx_audit_log (
	oid integer primary key not null identity(1,1),
	audit_date datetime not null,
	audit_event nvarchar(50) not null,
	audit_entity nvarchar(50),
	oid_audit_entity integer,
	oid_user_account integer,
	foreign key (oid_user_account) references gx_user_account(oid) on delete no action
);

