package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gx_voucher database table.
 * 
 */
@Entity
@Table(name = "gx_voucher")
@NamedQuery(name = "GxVoucher.findAll", query = "SELECT g FROM GxVoucher g")
public class GxVoucher extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	@Column(name = "voucher_date")
	private Timestamp voucherDate;

	@Column(name = "voucher_number")
	private String voucherNumber;

	//bi-directional many-to-many association to GxTransaction
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "gx_voucher_transaction_join", joinColumns = { @JoinColumn(name = "oid_voucher") }, inverseJoinColumns = { @JoinColumn(name = "oid_transaction") })
	private List<GxTransaction> gxTransactions = new ArrayList<>();

	public GxVoucher() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getVoucherDate() {
		return this.voucherDate;
	}

	public void setVoucherDate(Timestamp voucherDate) {
		this.voucherDate = voucherDate;
	}

	public String getVoucherNumber() {
		return this.voucherNumber;
	}

	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}

	public List<GxTransaction> getGxTransactions() {
		return this.gxTransactions;
	}

	public void setGxTransactions(List<GxTransaction> gxTransactions) {
		this.gxTransactions = gxTransactions;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

}