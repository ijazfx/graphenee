create table gx_folder (
	oid serial primary key,
	folder_id uuid not null,
	name varchar(255) not null,
	note varchar(200),
	tags text,
	oid_folder integer,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade
);

create table gx_folder_audit_log_join (
	oid_folder integer not null,
	oid_audit_log integer not null,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade,
	foreign key (oid_audit_log) references gx_audit_log(oid) on delete cascade on update cascade
);

create table gx_document_type (
	oid serial primary key,
	name varchar(20) not null,
	note varchar(200),
	tags text
);

create table gx_document (
	oid serial primary key,
	document_id uuid not null,
	name varchar(255) not null,
	note varchar(200),
	mime_type varchar(100),
	size bigint not null default 0,
	version_no integer not null default 0,
	path varchar(1000) not null,
	tags text,
	oid_document integer,
	oid_folder integer,
	foreign key (oid_document) references gx_document(oid) on delete cascade on update cascade,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade
);

create table gx_document_audit_log_join (
	oid_document integer not null,
	oid_audit_log integer not null,
	foreign key (oid_document) references gx_document(oid) on delete cascade on update cascade,
	foreign key (oid_audit_log) references gx_audit_log(oid) on delete cascade on update cascade
);