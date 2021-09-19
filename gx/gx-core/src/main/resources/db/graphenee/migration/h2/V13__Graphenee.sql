create table gx_password_policy (
	oid integer identity not null,
	oid_namespace integer not null,
    password_policy_name varchar(50),
    max_history integer not null default 6,
    max_age integer not null default 60,
    min_length integer not null default 8,
    is_user_username_allowed boolean not null default false,
    max_allowed_matching_user_name integer not null default 2,
    min_uppercase integer not null default 1,
    min_lowercase integer not null default 1,
    min_numbers integer not null default 1,
    min_special_charaters integer not null default 1,
    is_active boolean not null default false,
    primary key(oid),
    foreign key (oid_namespace) references gx_namespace(oid) on delete cascade on update cascade
);

create table gx_password_history (
	oid integer identity not null,
    oid_user_account integer not null,
    hashed_password varchar(200) not null,
    password_date timestamp not null,
    primary key(oid),
    foreign key(oid_user_account) references gx_user_account(oid) on delete cascade on update cascade
);