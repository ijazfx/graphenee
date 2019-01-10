create table gx_password_policy (
	oid integer primary key not null identity(1,1),
	oid_namespace integer not null,
    password_policy_name nvarchar(50),
    max_history integer not null default 6,
    max_age integer not null default 60,
    min_length integer not null default 8,
    is_user_username_allowed bit not null default 0,
    max_allowed_matching_user_name integer not null default 2,
    min_uppercase integer not null default 1,
    min_lowercase integer not null default 1,
    min_numbers integer not null default 1,
    min_special_charaters integer not null default 1,
    is_active bit not null default 0,
    foreign key (oid_namespace) references gx_namespace(oid) on delete cascade
);

create table gx_password_history (
	oid integer primary key not null identity(1,1),
    oid_user_account integer not null,
    hashed_password nvarchar(200) not null,
    password_date datetime not null,
    foreign key(oid_user_account) references gx_user_account(oid) on delete cascade
);