package io.graphenee.core.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.graphenee.core.model.GxMappedSuperclass;

@Entity
@Table(name = "gx_registered_device")
@NamedQuery(name = "GxRegisteredDevice.findAll", query = "select d from GxRegisteredDevice d")
public class GxRegisteredDevice extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "system_name")
	private String systemName;

	@Column(name = "device_token")
	private String deviceToken;

	@Column(name = "is_tablet")
	private Boolean isTablet;

	@Column(name = "brand")
	private String brand;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "owner_id")
	private String ownerId;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

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

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

}
