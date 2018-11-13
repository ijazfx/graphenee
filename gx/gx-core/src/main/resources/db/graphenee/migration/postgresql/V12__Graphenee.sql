create sequence gx_sms_provider_seq;

create table gx_sms_provider(
    oid integer not null default nextval('gx_sms_provider_seq'::regclass),
    provider_name varchar(50) not null,
    implementation_class varchar(200) not null,
    config_data bytea,
    is_primary boolean not null default false,
    is_active boolean not null default true,
    primary key(oid)
);
