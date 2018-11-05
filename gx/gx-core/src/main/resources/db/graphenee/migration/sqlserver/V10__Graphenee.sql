create table gx_access_key(
	oid integer not null identity(1,1),
    access_key uniqueidentifier not null,
    secret nvarchar(200) not null,
    is_active bit not null default 1,
    access_key_type integer,
    primary key(oid)
); 

create table gx_user_account_access_key_join(
	oid_user_account integer not null,
    oid_access_key integer not null,
    foreign key(oid_user_account) references gx_user_account(oid) on delete cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade
);

create table gx_access_key_security_group_join(
	oid_access_key integer not null,
    oid_security_group integer not null,
    foreign key(oid_security_group) references gx_security_group(oid) on delete cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade
);

create table gx_access_key_security_policy_join(
    oid_access_key integer not null, 
    oid_security_policy integer not null,
    foreign key(oid_security_policy) references gx_security_policy(oid) on delete cascade,
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade
);

create table gx_resource(
	oid integer not null identity(1,1),
    resource_name nvarchar(50),
    resource_desription nvarchar(100),
    is_active bit not null default 1,
    oid_namespace integer not null,
    foreign key(oid_namespace) references gx_namespace(oid),
    primary key(oid)
);

create table gx_access_log(
	oid integer not null identity(1,1),
	oid_access_key integer not null,
    oid_resource integer not null,
    access_time datetime not null,
    is_success bit not null default 0,
    access_type integer not null,
    primary key(oid),
    foreign key(oid_access_key) references gx_access_key(oid) on delete cascade,
    foreign key(oid_resource) references gx_resource(oid) on delete cascade
);
