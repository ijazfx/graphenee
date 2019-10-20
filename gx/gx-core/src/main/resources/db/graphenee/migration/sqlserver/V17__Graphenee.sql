alter table gx_user_account add oid_namespace integer;
alter table gx_user_account add constraint gx_user_account_namespace_fkey 
	foreign key (oid_namespace) references gx_namespace(oid) on delete set null on update cascade;