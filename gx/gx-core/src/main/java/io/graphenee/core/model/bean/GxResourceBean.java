package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxResourceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String resourceName;
	private String resourceDescription;
	private Boolean isActive = true;
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
}
