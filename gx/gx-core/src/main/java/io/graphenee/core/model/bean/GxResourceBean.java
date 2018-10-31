package io.graphenee.core.model.bean;

import java.io.Serializable;

public class GxResourceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String name;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
