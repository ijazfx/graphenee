package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.util.Collection;

import io.graphenee.core.model.BeanCollectionFault;
import io.graphenee.core.model.BeanFault;

public class GxStateBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Boolean isActive = true;
	private String stateCode;
	private String stateName;

	BeanFault<Integer, GxCountryBean> countryBeanFault;

	private BeanCollectionFault<GxCityBean> cityBeanColltionFault = BeanCollectionFault.emptyCollectionFault();

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

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public BeanFault<Integer, GxCountryBean> getCountryBeanFault() {
		return countryBeanFault;
	}

	public void setCountryBeanFault(BeanFault<Integer, GxCountryBean> countryBeanFault) {
		this.countryBeanFault = countryBeanFault;
	}

	public BeanCollectionFault<GxCityBean> getCityBeanColltionFault() {
		return cityBeanColltionFault;
	}

	public void setCityBeanColltionFault(BeanCollectionFault<GxCityBean> cityBeanColltionFault) {
		this.cityBeanColltionFault = cityBeanColltionFault;
	}

	public void setCityBeans(Collection<GxCityBean> cityBeans) {
		setCityBeanColltionFault(BeanCollectionFault.collectionFault(cityBeans));
		getCityBeanColltionFault().markAsModified();
	}

	public Collection<GxCityBean> getCityBeans() {
		return getCityBeanColltionFault().getBeans();
	}

	public String getCountryName() {
		return getCountryBeanFault() != null ? getCountryBeanFault().getBean().getCountryName() : null;
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
		GxStateBean other = (GxStateBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}