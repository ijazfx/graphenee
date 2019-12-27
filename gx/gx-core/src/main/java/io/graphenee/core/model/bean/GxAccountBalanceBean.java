package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.model.BeanFault;

public class GxAccountBalanceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Double openingBalance;
	private Timestamp openingYear;
	private BeanFault<Integer, GxAccountBean> gxAccountBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Timestamp getOpeningYear() {
		return openingYear;
	}

	public void setOpeningYear(Timestamp openingYear) {
		this.openingYear = openingYear;
	}

	public BeanFault<Integer, GxAccountBean> getGxAccountBeanFault() {
		return gxAccountBeanFault;
	}

	public void setGxAccountBeanFault(BeanFault<Integer, GxAccountBean> gxAccountBeanFault) {
		this.gxAccountBeanFault = gxAccountBeanFault;
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
		GxAccountBalanceBean other = (GxAccountBalanceBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
