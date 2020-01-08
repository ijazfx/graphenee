create sequence gx_account_type_seq;
create sequence gx_account_seq;
create sequence gx_voucher_seq;
create sequence gx_transaction_seq;
create sequence gx_account_balance_seq;
create sequence gx_account_configuration_seq;

create table gx_account_type (
	oid integer not null default nextval('gx_account_type_seq'::regclass),
	type_name varchar(50) not null,
	type_code varchar(2) not null,
	account_number_sequence integer,
	primary key (oid)
);

create table gx_account (
	oid integer not null default nextval('gx_account_seq'::regclass),
	account_code integer not null default 0,
	account_name varchar(50),
	oid_account_type integer not null,
	oid_parent integer,
	oid_namespace integer not null,
	foreign key (oid_account_type) references gx_account_type(oid) on delete restrict on update cascade,
	foreign key (oid_parent) references gx_account(oid) on delete restrict on update cascade,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
	primary key (oid)
);

create table gx_voucher (
	oid integer not null default nextval('gx_voucher_seq'::regclass),
	voucher_number varchar(20),
	voucher_date timestamp not null,
	description varchar(200) not null,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
	primary key (oid)
);

create table gx_transaction (
	oid integer not null default nextval('gx_transaction_seq'::regclass),
	transaction_date timestamp not null,
	amount double precision not null default 0,
	description varchar(200),
	is_archived boolean not null default false,
	oid_account integer not null,
	oid_namespace integer not null,
	foreign key (oid_account) references gx_account(oid) on delete restrict on update cascade,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
	primary key(oid)
);

create table gx_voucher_transaction_join (
	oid_voucher integer not null,
	oid_transaction integer not null,
	foreign key (oid_voucher) references gx_voucher(oid) on delete cascade on update cascade,
    foreign key (oid_transaction) references gx_transaction(oid) on delete cascade on update cascade
);

insert into gx_account_type (type_name, type_code) values ('Asset', 'AS');
insert into gx_account_type (type_name, type_code) values ('Liability', 'LI');
insert into gx_account_type (type_name, type_code) values ('Equity', 'EQ');
insert into gx_account_type (type_name, type_code) values ('Expense', 'EX');
insert into gx_account_type (type_name, type_code) values ('Income', 'IN');

create table gx_account_balance (
	oid integer not null default nextval('gx_account_balance_seq'::regclass),
	closing_balance double precision not null default 0,
	fiscal_year integer not null,
	oid_account integer not null,
	oid_namespace integer not null,
	foreign key (oid_account) references gx_account(oid) on delete restrict on update cascade,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
	primary key (oid)
);


create table gx_account_configuration (
	oid integer not null default nextval('gx_account_configuration_seq'::regclass),
	voucher_number integer not null default 0,
	fiscal_year_start timestamp not null,
	oid_namespace integer not null,
	foreign key (oid_namespace) references gx_namespace(oid) on delete restrict on update cascade,
	primary key (oid)
);

