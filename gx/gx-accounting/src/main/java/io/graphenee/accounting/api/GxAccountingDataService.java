package io.graphenee.accounting.api;

import java.util.List;

import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxAccountTypeBean;
import io.graphenee.core.model.bean.GxNamespaceBean;

public interface GxAccountingDataService {

	List<GxAccountTypeBean> findAllAccountTypes();

	GxAccountTypeBean createOrUpdate(GxAccountTypeBean bean);

	void delete(GxAccountTypeBean bean);

	GxAccountBean createOrUpdate(GxAccountBean bean);

	List<GxAccountBean> findAllAccounts();

	List<GxAccountBean> findAllAccountsByNamespace(GxNamespaceBean namespaceBean);

	void delete(GxAccountBean bean);

	GxAccountBean findByAccountNumberAndNamespace(Integer accountCode, GxNamespaceBean namespaceBean);

	GxAccountBean findByAccountNumber(Integer accountCode);
}
