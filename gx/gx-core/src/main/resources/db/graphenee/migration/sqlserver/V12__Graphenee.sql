create table gx_sms_provider(
    oid integer primary key not null identity(1,1),
    provider_name varchar(50) not null,
    implementation_class varchar(200) not null,
    config_data bytea,
    is_primary bit not null default 0,
    is_active bit not null default 0
);
