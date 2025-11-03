create table gx_domain (
	oid serial primary key,
	dns varchar(100) not null,
	is_active boolean not null default true,
	is_verified boolean not null default false,
	txt_record varchar(100),
	app_title varchar(100),
	app_logo bytea,
	oid_namespace INT not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

alter table gx_namespace add column app_title varchar(100);
alter table gx_namespace add column app_logo bytea;
