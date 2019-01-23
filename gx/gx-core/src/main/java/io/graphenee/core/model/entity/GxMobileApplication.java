package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.graphenee.core.model.GxMappedSuperclass;

@Entity
@Table(name = "gx_mobile_application")
@NamedQuery(name = "GxMobileApplication.findAll", query = "select m from GxMobileApplication m")
public class GxMobileApplication extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "application_name")
	private String applicationName;

	@Column(name = "is_active")
	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	@OneToMany(mappedBy = "gxMobileApplication")
	private List<GxRegisteredDevice> gxRegisteredDevices = new ArrayList<>();

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public List<GxRegisteredDevice> getGxRegisteredDevices() {
		return gxRegisteredDevices;
	}

	public void setGxRegisteredDevices(List<GxRegisteredDevice> gxRegisteredDevices) {
		this.gxRegisteredDevices = gxRegisteredDevices;
	}

	public GxRegisteredDevice addGxRegisteredDevice(GxRegisteredDevice gxRegisteredDevice) {
		getGxRegisteredDevices().add(gxRegisteredDevice);
		gxRegisteredDevice.setGxMobileApplication(this);

		return gxRegisteredDevice;
	}

	public GxRegisteredDevice removeGxRegisteredDevice(GxRegisteredDevice gxRegisteredDevice) {
		gxRegisteredDevices.remove(gxRegisteredDevice);
		gxRegisteredDevice.setGxMobileApplication(this);
		return gxRegisteredDevice;
	}

}
