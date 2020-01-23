package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanCollectionFault;

public class GxAccountTypeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String typeCode;
	private String typeName;
	private String accountNumberSequence;
	private BeanCollectionFault<GxAccountBean> accountBeanCollectionFault = BeanCollectionFault.emptyCollectionFault();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public BeanCollectionFault<GxAccountBean> getAccountBeanCollectionFault() {
		return accountBeanCollectionFault;
	}

	public void setAccountBeanCollectionFault(BeanCollectionFault<GxAccountBean> accountBeanCollectionFault) {
		this.accountBeanCollectionFault = accountBeanCollectionFault;
	}

	public String getAccountNumberSequence() {
		return accountNumberSequence;
	}

	public void setAccountNumberSequence(String accountNumberSequence) {
		this.accountNumberSequence = accountNumberSequence;
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
		GxAccountTypeBean other = (GxAccountTypeBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return typeName;
	}

}
