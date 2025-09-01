create table gx_file_tag (
	oid serial primary key,
	tag text
);

create table gx_file_tag_document_join (
	oid_tag integer,
	oid_document integer,
	constraint fk_gx_document foreign key (oid_document) references gx_document(oid),
	constraint fk_gx_file_tag foreign key (oid_tag) references gx_file_tag(oid)
);

create table gx_file_tag_folder_join (
	oid_tag integer,
	oid_folder integer,
	constraint fk_gx_document foreign key (oid_folder) references gx_folder(oid),
	constraint fk_gx_file_tag foreign key (oid_tag) references gx_file_tag(oid)
);