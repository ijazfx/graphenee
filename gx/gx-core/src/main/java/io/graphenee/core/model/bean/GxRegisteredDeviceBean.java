package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxRegisteredDeviceBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String systemName;
	private String deviceToken;
	private Boolean isTablet = false;
	private String brand;
	private Boolean isActive = true;
	private String ownerId;
	private BeanFault<Integer, GxNamespaceBean> namespaceFault;

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

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return namespaceFault;
	}

	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		this.namespaceFault = namespaceFault;
	}

	public boolean is_iOS() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("ios");
	}

	public boolean is_Android() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("android");
	}

}
