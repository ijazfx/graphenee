create sequence gx_namespace_seq;
create sequence gx_namespace_property_seq;
create sequence gx_supported_locale_seq;
create sequence gx_term_seq;
create sequence gx_email_template_seq;
create sequence gx_security_group_seq;
create sequence gx_security_policy_seq;
create sequence gx_security_policy_document_seq;
create sequence gx_gender_seq;
create sequence gx_user_account_seq;
create sequence gx_audit_log_seq;

create table gx_namespace (
	oid integer primary key not null default nextval('gx_namespace_seq'),
	namespace varchar(100) not null,
	namespace_description varchar(100),
	is_active boolean not null default true,
	is_protected boolean not null default false
);

create table gx_namespace_property (
	oid integer primary key not null default nextval('gx_namespace_property_seq'),
	property_key varchar(100) not null,
	property_value varchar(500) not null,
	property_default_value varchar(500) not null,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

insert into gx_namespace(namespace, namespace_description, is_protected) values ('com.graphenee.system', 'To be used by Graphenee', true);

create table gx_supported_locale (
	oid integer primary key not null default nextval('gx_supported_locale_seq'),
	locale_name varchar(50) not null,
	locale_code varchar(10) not null,
	is_left_to_right boolean not null default true,
	is_active boolean not null default true,
	is_protected boolean not null default false
);

insert into gx_supported_locale(locale_name, locale_code, is_protected) values ('English (United States)', 'en_US', true);

create table gx_term (
	oid integer primary key not null default nextval('gx_term_seq'),
	term_key varchar(100) not null,
	term_singular varchar(1000) not null,
	term_plural varchar(1000),
	is_active boolean not null default true,
	is_protected boolean not null default false,
	oid_supported_locale integer not null,
	oid_namespace integer not null,
	foreign key (oid_supported_locale) references gx_supported_locale(oid) on delete restrict on update cascade,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

create table gx_email_template (
	oid integer primary key not null default nextval('gx_email_template_seq'),
	template_name varchar(50) not null,
	subject varchar(500) not null,
	body text not null,
	cc_list varchar(500),
	bcc_list varchar(500),
	is_active boolean not null default true,
	is_protected boolean not null default false,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

create table gx_security_group (
	oid integer primary key not null default nextval('gx_security_group_seq'),
	security_group_name varchar(50) not null,
	priority integer not null default 0,
	is_active boolean not null default true,
	is_protected boolean not null default false,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

create table gx_security_policy (
	oid integer primary key not null default nextval('gx_security_policy_seq'),
	security_policy_name varchar(50) not null,
	priority integer not null default 0,
	is_active boolean not null default true,
	is_protected boolean not null default false,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

create table gx_security_policy_document (
	oid integer primary key not null default nextval('gx_security_policy_document_seq'),
	document_json text not null,
	is_default boolean not null default true,
	oid_security_policy integer not null,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete restrict on update cascade
);

create table gx_security_group_security_policy_join (
	oid_security_group integer not null,
	oid_security_policy integer not null,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade on update cascade,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete cascade on update cascade
);

create table gx_gender(
	oid integer primary key not null default nextval('gx_gender_seq'),
	gender_name varchar(10) not null,
	gender_code varchar(3) not null,
	is_active boolean not null default true,
	is_protected boolean not null default false
);

insert into gx_gender(gender_name, gender_code, is_protected) values ('Male', 'M', true);
insert into gx_gender(gender_name, gender_code, is_protected) values ('Female', 'F', true);

create table gx_user_account (
	oid integer primary key not null default nextval('gx_user_account_seq'),
	username varchar(50) not null,
	password varchar(200) not null,
	first_name varchar(30),
	last_name varchar(30),
	full_name_native varchar(100),
	email varchar(200),
	profile_image bytea,
	is_active boolean not null default true,
	is_locked boolean not null default false,
	is_protected boolean not null default false,
	is_password_change_required boolean default false,
	verification_token varchar(100),
	verification_token_expiry_date timestamp,
	account_activation_date timestamp,
	last_login_date timestamp,
	last_login_failed_date timestamp,
	count_login_failed integer not null default 0,
	oid_gender integer,
	foreign key (oid_gender) references gx_gender(oid) on delete restrict on update cascade
);

insert into gx_user_account (username, password, is_password_change_required, is_protected) values ('admin', 'change_on_install', true, true);

create table gx_user_account_security_group_join (
	oid_user_account integer not null,
	oid_security_group integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade on update cascade,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade on update cascade
);

create table gx_user_account_security_policy_join (
	oid_user_account integer not null,
	oid_security_policy integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade on update cascade,
	foreign key (oid_security_policy) references gx_security_policy(oid) on delete cascade on update cascade	
);

create table gx_audit_log (
	oid integer primary key not null default nextval('gx_audit_log_seq'),
	audit_date timestamp not null,
	audit_event varchar(50) not null,
	audit_entity varchar(50),
	oid_audit_entity integer,
	oid_user_account integer,
	foreign key (oid_user_account) references gx_user_account(oid) on delete restrict on update cascade
);

