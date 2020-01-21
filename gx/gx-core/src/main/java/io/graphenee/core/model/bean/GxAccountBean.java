package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxAccountBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID uuid = UUID.randomUUID();

	private Integer oid;
	private Integer accountCode;
	private String accountName;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;
	private BeanFault<Integer, GxAccountTypeBean> gxAccountTypeBeanFault;
	private BeanFault<Integer, GxAccountBean> gxParentAccountBeanFault;
	private BeanCollectionFault<GxAccountBean> gxChildAccountBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxTransactionBean> gxTransactionBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();
	private BeanCollectionFault<GxAccountBalanceBean> gxAccountBalanceBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();

	private String parentAccountName;
	private String accountType;
	private Double closingBalance;
	private Integer year;

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
		if (accountType != null)
			return accountType;
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

	public List<GxAccountBean> getDirectChildAccounts() {
		return getGxChildAccountBeanCollectionFault().getBeans().stream().collect(Collectors.toList());
	}

	public String getAccountNameWithCode() {
		return accountCode + " - " + accountName;
	}

	public String getIndentedTitle() {
		StringBuilder builder = new StringBuilder();
		GxAccountBean account = gxParentAccountBeanFault != null ? gxParentAccountBeanFault.getBean() : null;
		boolean first = true;
		while (account != null) {
			if (first) {
				builder.append("\u251c\u2500");
				first = false;
			} else {
				builder.append("\u253c\u2500");
			}
			account = account.gxParentAccountBeanFault != null ? account.gxParentAccountBeanFault.getBean() : null;
		}
		builder.append(" " + accountName);
		return builder.toString();
	}

	public String getParentAccountName() {
		return parentAccountName;
	}

	public void setParentAccountName(String parentAccountName) {
		this.parentAccountName = parentAccountName;
	}

	public Double getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(Double closingBalance) {
		this.closingBalance = closingBalance;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
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
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
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
