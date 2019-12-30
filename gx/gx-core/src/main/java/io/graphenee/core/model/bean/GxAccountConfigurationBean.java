package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.model.BeanFault;

public class GxAccountConfigurationBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Timestamp fiscalYearStart;
	private Integer voucherNumber;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getFiscalYearStart() {
		return fiscalYearStart;
	}

	public void setFiscalYearStart(Timestamp fiscalYearStart) {
		this.fiscalYearStart = fiscalYearStart;
	}

	public Integer getVoucherNumber() {
		return voucherNumber;
	}

	public void setVoucherNumber(Integer voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
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
		GxAccountConfigurationBean other = (GxAccountConfigurationBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
