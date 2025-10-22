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