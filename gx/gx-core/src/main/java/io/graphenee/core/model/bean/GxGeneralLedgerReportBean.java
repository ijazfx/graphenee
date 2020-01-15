package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class GxGeneralLedgerReportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String parentAccount;
	private List<GxGeneralLedgerBean> generalLedgerList = new ArrayList<>();
	private JRBeanCollectionDataSource generalLedgerDataSource;

	public String getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(String parentAccount) {
		this.parentAccount = parentAccount;
	}

	public List<GxGeneralLedgerBean> getGeneralLedgerList() {
		return generalLedgerList;
	}

	public void setGeneralLedgerList(List<GxGeneralLedgerBean> generalLedgerList) {
		this.generalLedgerList = generalLedgerList;
	}

	public JRBeanCollectionDataSource getGeneralLedgerDataSource() {
		return new JRBeanCollectionDataSource(generalLedgerList);
	}

}
