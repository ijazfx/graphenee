update gx_user_account set is_password_change_required = false where username = 'admin';
update gx_user_account set password = 'admin' where password = 'change_on_install' or password = '2f6f5258456156504478424271715756614457534a516d6c4d366f3d';
update gx_user_account set oid_namespace = (select oid from gx_namespace where namespace = 'io.graphenee.system') where oid_namespace is null;