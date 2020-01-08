package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxAccountBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Integer accountCode;
	private String accountName;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;
	private BeanFault<Integer, GxAccountTypeBean> gxAccountTypeBeanFault;
	private BeanFault<Integer, GxAccountBean> gxParentAccountBeanFault;
	private BeanCollectionFault<GxAccountBean> gxChildAccountBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxTransactionBean> gxTransactionBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxAccountBalanceBean> gxAccountBalanceBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Integer getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(Integer accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public BeanFault<Integer, GxAccountBean> getGxParentAccountBeanFault() {
		return gxParentAccountBeanFault;
	}

	public void setGxParentAccountBeanFault(BeanFault<Integer, GxAccountBean> gxParentAccountBeanFault) {
		this.gxParentAccountBeanFault = gxParentAccountBeanFault;
	}

	public BeanFault<Integer, GxAccountTypeBean> getGxAccountTypeBeanFault() {
		return gxAccountTypeBeanFault;
	}

	public void setGxAccountTypeBeanFault(BeanFault<Integer, GxAccountTypeBean> gxAccountTypeBeanFault) {
		this.gxAccountTypeBeanFault = gxAccountTypeBeanFault;
	}

	public BeanCollectionFault<GxAccountBean> getGxChildAccountBeanCollectionFault() {
		return gxChildAccountBeanCollectionFault;
	}

	public void setGxChildAccountBeanCollectionFault(BeanCollectionFault<GxAccountBean> gxChildAccountBeanCollectionFault) {
		this.gxChildAccountBeanCollectionFault = gxChildAccountBeanCollectionFault;
	}

	public BeanCollectionFault<GxTransactionBean> getGxTransactionBeanCollectionFault() {
		return gxTransactionBeanCollectionFault;
	}

	public void setGxTransactionBeanCollectionFault(BeanCollectionFault<GxTransactionBean> gxTransactionBeanCollectionFault) {
		this.gxTransactionBeanCollectionFault = gxTransactionBeanCollectionFault;
	}

	public BeanCollectionFault<GxAccountBalanceBean> getGxAccountBalanceBeanCollectionFault() {
		return gxAccountBalanceBeanCollectionFault;
	}

	public void setGxAccountBalanceBeanCollectionFault(BeanCollectionFault<GxAccountBalanceBean> gxAccountBalanceBeanCollectionFault) {
		this.gxAccountBalanceBeanCollectionFault = gxAccountBalanceBeanCollectionFault;
	}

	public String getAccountType() {
		return getGxAccountTypeBeanFault() != null ? getGxAccountTypeBeanFault().getBean().getTypeName() : null;
	}

	public String getParentAccount() {
		return getGxParentAccountBeanFault() != null ? getGxParentAccountBeanFault().getBean().getAccountName() : null;
	}

	public List<GxAccountBean> getAllChildAccounts() {
		return getAllChildAccounts(this);
	}

	private List<GxAccountBean> getAllChildAccounts(GxAccountBean parent) {
		List<GxAccountBean> childAccounts = new ArrayList<>();

		for (GxAccountBean child : parent.getGxChildAccountBeanCollectionFault().getBeans()) {
			childAccounts.add(child);
			childAccounts.addAll(getAllChildAccounts(child));
		}

		return childAccounts;
	}

	public String getAccountNameWithCode() {
		return accountCode + " - " + accountName;
	}

	public String getIndentedTitle() {
		return accountCode + "|- " + accountName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GxAccountBean other = (GxAccountBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return accountCode + " - " + accountName;
	}

}
