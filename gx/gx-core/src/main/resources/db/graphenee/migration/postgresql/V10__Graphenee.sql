create sequence gx_access_key_seq;
create sequence gx_resource_seq;
create sequence gx_access_log_sequence;

create table gx_access_key(
    oid integer not null default nextval('gx_access_key_seq'::regclass),
    access_key uuid not null,
    secret varchar(200) not null,
    is_active boolean not null default true,
    access_key_type integer,
    primary key(oid)
); 

create table gx_user_account_access_key_join(
	oid_user_account integer not null,
    oid_access_key integer not null,
    foreign key(oid_user_account) references gx_user_account(oid) on delete cascade on update cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade on update cascade
);

create table gx_access_key_security_group_join(
	oid_access_key integer not null,
    oid_security_group integer not null,
    foreign key(oid_security_group) references gx_security_group(oid) on delete cascade on update cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade on update cascade
);

create table gx_access_key_security_policy_join(
    oid_access_key integer not null, 
    oid_security_policy integer not null,
    foreign key(oid_security_policy) references gx_security_policy(oid) on delete cascade on update cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade on update cascade
);

create table gx_resource(
	oid integer not null default nextval('gx_resource_seq'::regclass),
    resource_name varchar(50),
    resource_desription varchar(100),
    is_active boolean not null default true,
    oid_namespace integer not null,
    foreign key(oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
    primary key(oid)
);

create table gx_access_log(
	oid integer not null default nextval('gx_access_log_sequence'::regclass),
	oid_access_key integer not null,
    oid_resource integer not null,
    access_time timestamp not null,
    is_success boolean not null default false,
    access_type integer not null,
    primary key(oid),
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade on update cascade,
    foreign key(oid_resource) references gx_resource(oid) on delete cascade on update cascade
);
