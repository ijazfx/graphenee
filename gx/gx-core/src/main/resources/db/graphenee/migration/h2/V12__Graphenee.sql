create table gx_sms_provider(
    oid integer identity not null,
    provider_name varchar(50) not null,
    implementation_class varchar(200) not null,
    config_data bytea,
    is_primary boolean not null default false,
    is_active boolean not null default true,
    primary key(oid)
);
