package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxRegisteredDeviceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String systemName;
	private String uniqueId;
	private Boolean isTablet = false;
	private String brand;
	private Boolean isActive = true;
	private String ownerId;
	private BeanFault<Integer, GxMobileApplicationBean> gxMobileApplicationBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Boolean getIsTablet() {
		return isTablet;
	}

	public void setIsTablet(Boolean isTablet) {
		this.isTablet = isTablet;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public BeanFault<Integer, GxMobileApplicationBean> getGxMobileApplicationBeanFault() {
		return gxMobileApplicationBeanFault;
	}

	public void setGxMobileApplicationBeanFault(BeanFault<Integer, GxMobileApplicationBean> gxMobileApplicationBeanFault) {
		this.gxMobileApplicationBeanFault = gxMobileApplicationBeanFault;
	}

	public String getMobileApplicationName() {
		return getGxMobileApplicationBeanFault() != null ? gxMobileApplicationBeanFault.getBean().getApplicationName() : "N/A";
	}
}
