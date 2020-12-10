create table gx_mobile_application (
	oid integer primary key not null identity(1,1),
	application_name nvarchar(50),
	is_active bit not null default 1,
	oid_namespace integer not null,
	foreign key(oid_namespace) references gx_namespace(oid) on delete no action
);

create table gx_registered_device (
	oid integer primary key not null identity(1,1),
	oid_mobile_application integer not null,
	system_name nvarchar(50),
	unique_id nvarchar(100),
	is_tablet bit not null default 0,
	brand nvarchar(50),
	is_active bit not null default 1,
	owner_id nvarchar(100),
	foreign key(oid_mobile_application) references gx_mobile_application(oid) on delete no action
);

create index owner_id_index on gx_registered_device(owner_id);