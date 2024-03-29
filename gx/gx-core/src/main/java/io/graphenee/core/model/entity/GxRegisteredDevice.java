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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "gx_registered_device")
@NamedQuery(name = "GxRegisteredDevice.findAll", query = "select d from GxRegisteredDevice d")
public class GxRegisteredDevice extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Include
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

	public GxRegisteredDevice() {
		isActive = true;
		isTablet = false;
	}

	public boolean is_iOS() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("ios");
	}

	public boolean is_Android() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("android");
	}

}
