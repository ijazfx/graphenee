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
	t.oid_namespace,
	v.oid as oid_voucher
from gx_transaction as t, gx_account as ac, gx_account_type as act, gx_voucher as v, gx_voucher_transaction_join as vtj
where t.oid_account = ac.oid and act.oid = ac.oid_account_type and v.oid = vtj.oid_voucher and t.oid = vtj.oid_transaction
order by t.transaction_date asc
with no data;
create index transaction_date on gx_general_ledger_view(transaction_date);
REFRESH MATERIALIZED VIEW gx_general_ledger_view;

drop materialized view if exists gx_trial_balance_view;
create materialized view gx_trial_balance_view as
select 
	row_number() over() oid,
	date_trunc('month',t.transaction_date) as month,
	ac.account_code,
	ac.account_name,
	t.oid_account,
	ac.oid_account_type,
	act.type_name as account_type_name,
	sum(coalesce(t.amount)) as amount,
	t.oid_namespace
from gx_transaction as t, gx_account as ac,  gx_account_type as act
where t.oid_account = ac.oid and ac.oid_account_type = act.oid
group by  ac.account_code, ac.account_name,t.oid_account, account_type_name, ac.oid_account_type, t.oid_namespace, month
with no data;
create index oid_account on gx_trial_balance_view(oid_account);
REFRESH MATERIALIZED VIEW gx_trial_balance_view;

drop materialized view if exists gx_balance_sheet_view;
create materialized view gx_balance_sheet_view as
select 
	row_number() over() oid,
	date_trunc('month',t.transaction_date) as month,
	ac.account_name,
	ac.oid as oid_account,
	ac.oid_parent as oid_parent_account,
	(select pr.account_name from gx_account pr where ac.oid_parent = pr.oid) as parent_account_name,
	ac.oid_account_type,
	act.type_name as account_type_name,
	act.type_code as account_type_code,
	sum(t.amount) as amount,
	t.oid_namespace
from gx_transaction as t, gx_account as ac,  gx_account_type as act
where t.oid_account = ac.oid and act.oid = ac.oid_account_type
group by month, ac.account_name, ac.oid, oid_parent_account,account_type_name, account_type_code,t.oid_namespace
with no data;
create index oid on gx_balance_sheet_view(oid);
REFRESH MATERIALIZED VIEW gx_balance_sheet_view;
