CREATE TABLE gx_document_folder_tag (
    oid AUTO_INCREMENT PRIMARY KEY,
    tag VARCHAR(255)
);

alter table gx_folder add constraint fk_gx_document_folder_tag foreign key (tag_oid) references gx_document_folder_tag(oid);
alter table gx_document add constraint fk_gx_document_folder_tag foreign key (tag_oid) references gx_document_folder_tag(oid);
