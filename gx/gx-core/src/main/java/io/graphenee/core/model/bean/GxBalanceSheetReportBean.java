package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GxBalanceSheetReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String parentAccount;
	private List<GxBalanceSheetBean> balanceSheetList = new ArrayList<>();
	private JRBeanCollectionDataSource balanceSheetDataSource;

	public String getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(String parentAccount) {
		this.parentAccount = parentAccount;
	}

	public List<GxBalanceSheetBean> getBalanceSheetList() {
		return balanceSheetList;
	}

	public void setBalanceSheetList(List<GxBalanceSheetBean> balanceSheetList) {
		this.balanceSheetList = balanceSheetList;
	}

	public JRBeanCollectionDataSource getBalanceSheetDataSource() {
		return new JRBeanCollectionDataSource(balanceSheetList);
	}

}
