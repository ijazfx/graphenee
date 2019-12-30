drop materialized view if exists gx_general_ledger_view;
create materialized view gx_general_ledger_view as
select 
	row_number() over() oid,
	t.transaction_date, 
	ac.account_name,
	ac.oid as oid_account,
	ac.oid_account_type,
	act.type_name as account_type_name,
	t.description as description, 
	t.amount,
	t.oid_namespace
from gx_transaction as t, gx_account as ac, gx_account_type as act
where t.oid_account = ac.oid and act.oid = ac.oid_account_type
order by t.transaction_date asc
with no data;
create index transaction_date on gx_general_ledger_view(transaction_date);
REFRESH MATERIALIZED VIEW gx_general_ledger_view;

drop materialized view if exists gx_trial_balance_view;
create materialized view gx_trial_balance_view as
select 
	row_number() over() oid,
	extract(month from t.transaction_date) as month, 
	extract(year from t.transaction_date) as year, 
	ac.account_name,
	ac.oid as oid_account,
	ac.oid_account_type,
	act.type_name as account_type_name,
	(case when t.amount < 0 then sum(t.amount) else 0.0 end) as credit,
	(case when t.amount > 0 then sum(t.amount) else 0.0 end) as debit,
	t.oid_namespace
from gx_transaction as t, gx_account as ac,  gx_account_type as act
where t.oid_account = ac.oid and act.oid = ac.oid_account_type
group by month, year, ac.account_name, t.amount, ac.oid, account_type_name, t.oid_namespace
with no data;
create index oid_account on gx_trial_balance_view(oid_account);
REFRESH MATERIALIZED VIEW gx_trial_balance_view;