package io.graphenee.core.model.entity;

import java.io.Serializable;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_registered_device")
public class GxRegisteredDevice extends GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	private String systemName;
	private String deviceToken;
	private String brand;
	private String ownerId;

	private Boolean isTablet;
	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

	public boolean is_iOS() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("ios");
	}

	public boolean is_Android() {
		return getSystemName() != null && getSystemName().trim().toLowerCase().equals("android");
	}

}
