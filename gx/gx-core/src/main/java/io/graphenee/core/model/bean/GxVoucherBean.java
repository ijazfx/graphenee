package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxVoucherBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Timestamp voucherDate;
	private String voucherNumber;
	private String description;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;
	private BeanCollectionFault<GxTransactionBean> gxTransactionBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Timestamp voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public BeanCollectionFault<GxTransactionBean> getGxTransactionBeanCollectionFault() {
		return gxTransactionBeanCollectionFault;
	}

	public void setGxTransactionBeanCollectionFault(BeanCollectionFault<GxTransactionBean> gxTransactionBeanCollectionFault) {
		this.gxTransactionBeanCollectionFault = gxTransactionBeanCollectionFault;
	}

	public Double getDebitTotal() {
		return getGxTransactionBeanCollectionFault().getBeans().stream().mapToDouble(GxTransactionBean::getDebit).sum();
	}

	public Double getCreditTotal() {
		return getGxTransactionBeanCollectionFault().getBeans().stream().mapToDouble(GxTransactionBean::getCredit).sum();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		GxVoucherBean other = (GxVoucherBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
