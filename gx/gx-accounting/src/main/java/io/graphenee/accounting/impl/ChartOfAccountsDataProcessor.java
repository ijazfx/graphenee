package io.graphenee.accounting.impl;

import java.util.HashMap;
import java.util.Map;

import io.graphenee.accounting.api.GxAccountingDataService;
import io.graphenee.core.model.api.GxImportDataProcessor;
import io.graphenee.core.model.bean.GxAccountBean;
import io.graphenee.core.model.bean.GxImportChartOfAccountBean;

public class ChartOfAccountsDataProcessor extends GxImportDataProcessor<GxAccountBean> {

	GxAccountingDataService accountingDataService;
	GxImportChartOfAccountBean importCOABean;

	public ChartOfAccountsDataProcessor(GxAccountingDataService accountingDataService, GxImportChartOfAccountBean importCOABean) {
		super(GxAccountBean.class);
		this.accountingDataService = accountingDataService;
		this.importCOABean = importCOABean;
	}

	public ChartOfAccountsDataProcessor() {
		super(GxAccountBean.class);
	}

	@Override
	protected GxAccountBean processRowData(String[] rowData) {
		GxAccountBean bean = new GxAccountBean();

		String accountCode = rowData[getColumnIndex("account_code")];
		if (accountCode != null)
			bean.setAccountCode(Integer.parseInt(accountCode.trim()));

		String accountName = rowData[getColumnIndex("account_name")];
		if (accountName != null)
			bean.setAccountName(accountName.trim());

		String accountType = rowData[getColumnIndex("account_type")];
		if (accountType != null)
			bean.setAccountType(accountType.trim());

		String parentAccount = rowData[getColumnIndex("parent_account")];
		if (parentAccount != null)
			bean.setParentAccountName(parentAccount.trim());

		String closingBalance = rowData[getColumnIndex("closing_balance")];
		if (closingBalance != null)
			bean.setClosingBalance(Double.parseDouble(closingBalance.trim()));

		return bean;
	}

	@Override
	public void saveData() {
		Map<GxAccountBean, GxAccountBean> accountMap = new HashMap<>();
		if (!getImportDataBeans().isEmpty()) {
			for (GxAccountBean bean : getImportDataBeans()) {
				if (bean.getParentAccountName() != null) {
					GxAccountBean parentAccount = getImportDataBeans().stream().filter(ac -> ac.getAccountName().equalsIgnoreCase(bean.getParentAccountName())).findFirst()
							.orElse(null);
					accountMap.put(bean, parentAccount);
				}
			}
		}
		accountingDataService.importAccounts(accountMap, importCOABean);
	}

	@Override
	public String[] getVisibleProperties() {
		return new String[] { "accountType", "accountCode", "accountName", "parentAccountName", "closingBalance" };
	}

	@Override
	public String[] requiredColoumnHeader() {
		return new String[] { "account_type", "account_code", "account_name", "parent_account", "closing_balance" };
	}

}
