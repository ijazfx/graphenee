package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gx_account_configuration database table.
 * 
 */
@Entity
@Table(name = "gx_account_configuration")
@NamedQuery(name = "GxAccountConfiguration.findAll", query = "SELECT g FROM GxAccountConfiguration g")
public class GxAccountConfiguration extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "fiscal_year_start")
	private Timestamp fiscalYearStart;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	@Column(name = "voucher_number")
	private Integer voucherNumber;

	public GxAccountConfiguration() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getFiscalYearStart() {
		return this.fiscalYearStart;
	}

	public void setFiscalYearStart(Timestamp fiscalYearStart) {
		this.fiscalYearStart = fiscalYearStart;
	}

	public Integer getVoucherNumber() {
		return this.voucherNumber;
	}

	public void setVoucherNumber(Integer voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

}