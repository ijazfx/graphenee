drop table gx_registered_device;
drop table gx_mobile_application;

create table gx_registered_device (
	oid integer primary key not null identity(1,1),
	system_name nvarchar(50),
	device_token nvarchar(200),
	is_tablet bit not null default 0,
	brand nvarchar(50),
	is_active bit not null default 1,
	owner_id nvarchar(100),
	oid_namespace integer not null,
	foreign key(oid_namespace) references gx_namespace(oid) on delete no action
);

create index owner_id_index on gx_registered_device(owner_id);