alter table gx_namespace add column users_count integer;

create table user_session_detail(
    oid serial primary key,
    identifier text,
    oid_user integer,
    oid_namespace integer,
    is_signed_in boolean,
    signedin_at timestamp,
    last_sync timestamp,
    foreign key (oid_user) references gx_user_account(oid)  on delete restrict on update cascade,
    foreign key (oid_namespace) references gx_namespace(oid)  on delete restrict on update cascade
);