create table gx_security_group_document_join (
	oid_security_group integer not null,
	oid_document integer not null,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade on update cascade,
	foreign key (oid_document) references gx_document(oid) on delete cascade on update cascade
);

create table gx_security_group_folder_join (
	oid_security_group integer not null,
	oid_folder integer not null,
	foreign key (oid_security_group) references gx_security_group(oid) on delete cascade on update cascade,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade
);

create table gx_user_account_document_join (
	oid_user_account integer not null,
	oid_document integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade on update cascade,
	foreign key (oid_document) references gx_document(oid) on delete cascade on update cascade
);

create table gx_user_account_folder_join (
	oid_user_account integer not null,
	oid_folder integer not null,
	foreign key (oid_user_account) references gx_user_account(oid) on delete cascade on update cascade,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade
);

alter table gx_document add column oid_owner integer;
alter table gx_document add constraint gx_document_oid_owner_fkey foreign key(oid_owner) references gx_user_account(oid) on delete set null on update cascade;

alter table gx_folder add column oid_owner integer;
alter table gx_folder add constraint gx_folder_oid_owner_fkey foreign key(oid_owner) references gx_user_account(oid) on delete set null on update cascade;

drop table gx_file_tag cascade;
drop table gx_file_tag_document_join cascade;
drop table gx_file_tag_folder_join cascade;

create table gx_tag (
	oid serial primary key,
	tag varchar(100) not null,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade
);

create table gx_document_tag_join (
	oid_document integer not null,
	oid_tag integer not null,
	foreign key (oid_document) references gx_document(oid) on delete cascade on update cascade,
	foreign key (oid_tag) references gx_tag(oid) on delete cascade on update cascade
);

create table gx_folder_tag_join (
	oid_folder integer not null,
	oid_tag integer not null,
	foreign key (oid_folder) references gx_folder(oid) on delete cascade on update cascade,
	foreign key (oid_tag) references gx_tag(oid) on delete cascade on update cascade
);