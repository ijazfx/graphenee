create table gx_sms_provider(
    oid integer primary key not null identity(1,1),
    provider_name nvarchar(50) not null,
    implementation_class nvarchar(200) not null,
    config_data varbinary(max),
    is_primary bit not null default 0,
    is_active bit not null default 0
);