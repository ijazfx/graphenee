create sequence gx_mobile_application_seq;
create sequence gx_registered_device_seq;

create table gx_mobile_application (
	oid integer not null default nextval('gx_mobile_application_seq'::regclass),
	application_name varchar(50),
	is_active boolean not null default true,
	oid_namespace integer not null,
	foreign key(oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
    primary key(oid)
);

create table gx_registered_device (
	oid integer not null default nextval('gx_registered_device_seq'::regclass),
	oid_mobile_application integer not null,
	system_name varchar(50),
	unique_id varchar(100),
	is_tablet boolean not null default false,
	brand varchar(50),
	is_active boolean not null default true,
	owner_id varchar(100),
	foreign key(oid_mobile_application) references gx_mobile_application(oid) on delete restrict on update cascade,
	primary key(oid)
);

create index owner_id_index on gx_registered_device(owner_id);