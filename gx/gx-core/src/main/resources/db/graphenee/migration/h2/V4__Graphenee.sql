create sequence gx_saved_query_seq;

create table gx_saved_query(
	oid integer identity not null,
	query_name varchar(50) not null,
	query_bean_json text not null,
	query_bean_class_name varchar(200) not null,
	additional_info varchar(1000),
	target_user varchar(200),
	primary key (oid)
);
