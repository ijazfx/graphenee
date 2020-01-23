package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import io.graphenee.core.model.BeanFault;

public class GxTransactionBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID uuid = UUID.randomUUID();

	private Integer oid;
	private Double debit = 0.0;
	private Double credit = 0.0;
	private String description;
	private Boolean isArchived = false;
	private Timestamp transactionDate;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;
	private BeanFault<Integer, GxAccountBean> gxAccountBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public BeanFault<Integer, GxAccountBean> getGxAccountBeanFault() {
		return gxAccountBeanFault;
	}

	public void setGxAccountBeanFault(BeanFault<Integer, GxAccountBean> gxAccountBeanFault) {
		this.gxAccountBeanFault = gxAccountBeanFault;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	public String getAccountName() {
		return getGxAccountBeanFault() != null ? getGxAccountBeanFault().getBean().getAccountName() : null;
	}

	public String getAccountCode() {
		return getGxAccountBeanFault() != null ? getGxAccountBeanFault().getBean().getAccountCode() : null;
	}

	public Double getAmount() {
		return debit > 0 ? debit : (-credit);
	}

	@Override
	public String toString() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		GxTransactionBean other = (GxTransactionBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
