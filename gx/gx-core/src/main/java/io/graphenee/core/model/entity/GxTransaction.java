package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gx_transaction database table.
 * 
 */
@Entity
@Table(name = "gx_transaction")
@NamedQuery(name = "GxTransaction.findAll", query = "SELECT g FROM GxTransaction g")
public class GxTransaction extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	private Double amount;

	private String description;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	@Column(name = "transaction_date")
	private Timestamp transactionDate;

	//bi-directional many-to-one association to GxAccount
	@ManyToOne
	@JoinColumn(name = "oid_account")
	private GxAccount gxAccount;

	//bi-directional many-to-many association to GxVoucher
	@ManyToMany(mappedBy = "gxTransactions")
	private List<GxVoucher> gxVouchers = new ArrayList<>();

	public GxTransaction() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public GxAccount getGxAccount() {
		return this.gxAccount;
	}

	public void setGxAccount(GxAccount gxAccount) {
		this.gxAccount = gxAccount;
	}

	public List<GxVoucher> getGxVouchers() {
		return this.gxVouchers;
	}

	public void setGxVouchers(List<GxVoucher> gxVouchers) {
		this.gxVouchers = gxVouchers;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}