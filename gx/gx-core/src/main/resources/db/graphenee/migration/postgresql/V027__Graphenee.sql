create table gx_domain (
	oid serial primary key,
	dns varchar(100) not null,
	is_active boolean not null default true,
	is_verified boolean not null default false,
	txt_record varchar(100),
	oid_namespace INT not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);
