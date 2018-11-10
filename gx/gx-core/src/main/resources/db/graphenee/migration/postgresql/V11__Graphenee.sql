create sequence gx_currency_seq;

create table gx_currency(
    oid integer not null default nextval('gx_currency_seq'::regclass),
    alpha3_code varchar(3) not null,
    currency_name varchar(50) not null,
    currency_symbol varchar(10) not null,
    numeric_code integer not null,
    is_active boolean not null default true,
    primary key(oid)
);

insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (156, 'CNY', 'Yuan', '¥');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (840, 'USD', 'United States Dollar', '$');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (124, 'CAD', 'Canadian Dollar', '$');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (36, 'AUD', 'Australian Dollar', '$');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (458, 'MYR', 'Malaysian Ringgit', 'RM');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (643, 'RUB', 'Russian Ruble', 'p.');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (710, 'ZAR', 'Rand', 'R');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (554, 'NZD', 'New Zealand Dollar', '$');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (978, 'EUR', 'Euro', '€');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (826, 'GBP', 'Pound Sterling', '£');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (949, 'TRY', 'Turkish Lira', '₤');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (586, 'PKR', 'Pakistan Rupee', 'Rs.');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (356, 'INR', 'Indian Rupee', '₹');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (634, 'QAR', 'Qatari Rial', 'ر.ق');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (784, 'AED', 'UAE Dirham', 'د.إ');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (682, 'SAR', 'Saudi Riyal', 'ر.س');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (512, 'OMR', 'Rial Omani', 'ر.ع');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (48, 'BHD', 'Bahraini Dinar', 'ب.د');
insert into gx_currency (numeric_code, alpha3_code, currency_name, currency_symbol) values (414, 'KWD', 'Kuwaiti Dinar', 'د.ك');
