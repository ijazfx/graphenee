create table gx_saved_query(
	oid integer not null identity(1,1),
	query_name nvarchar(50) not null,
	query_bean_json nvarchar(max) not null,
	query_bean_class_name nvarchar(200) not null,
	additional_info nvarchar(1000),
	target_user nvarchar(200),
	primary key (oid)
);
