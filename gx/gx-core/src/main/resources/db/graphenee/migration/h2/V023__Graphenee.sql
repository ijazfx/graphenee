create table gx_file_tag (
	oid INT AUTO_INCREMENT primary key,
	tag text
);

create table gx_file_tag_document_join (
	oid_tag INT,
	oid_document INT,
	foreign key (oid_document) references gx_document(oid),
	foreign key (oid_tag) references gx_file_tag(oid)
);

create table gx_file_tag_folder_join (
	oid_tag INT,
	oid_folder INT,
	foreign key (oid_folder) references gx_folder(oid),
	foreign key (oid_tag) references gx_file_tag(oid)
);