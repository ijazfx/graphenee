package com.graphenee.core.model.bean;

import java.io.Serializable;

import com.graphenee.core.model.BeanFault;

public class GxCityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Boolean isActive = true;
	private String cityName;

	BeanFault<Integer, GxCountryBean> countryBeanFault;
	BeanFault<Integer, GxStateBean> stateBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public BeanFault<Integer, GxCountryBean> getCountryBeanFault() {
		return countryBeanFault;
	}

	public void setCountryBeanFault(BeanFault<Integer, GxCountryBean> countryBeanFault) {
		this.countryBeanFault = countryBeanFault;
	}

	public BeanFault<Integer, GxStateBean> getStateBeanFault() {
		return stateBeanFault;
	}

	public void setStateBeanFault(BeanFault<Integer, GxStateBean> stateBeanFault) {
		this.stateBeanFault = stateBeanFault;
	}

	public String getCountryName() {
		return getCountryBeanFault() != null ? getCountryBeanFault().getBean().getCountryName() : null;
	}

	public String getStateName() {
		return getStateBeanFault() != null ? getStateBeanFault().getBean().getStateName() : null;
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
		GxCityBean other = (GxCityBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}