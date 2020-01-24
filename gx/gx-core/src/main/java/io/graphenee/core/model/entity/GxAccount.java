package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

/**
 * The persistent class for the gx_account database table.
 * 
 */
@Entity
@Table(name = "gx_account")
@NamedQuery(name = "GxAccount.findAll", query = "SELECT g FROM GxAccount g")
public class GxAccount extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@Column(name = "account_code")
	private String accountCode;

	@Column(name = "account_name")
	private String accountName;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;

	//bi-directional many-to-one association to GxAccount
	@ManyToOne
	@JoinColumn(name = "oid_parent")
	private GxAccount gxParentAccount;

	//bi-directional many-to-one association to GxAccount
	@OneToMany(mappedBy = "gxParentAccount")
	private List<GxAccount> gxChildAccounts = new ArrayList<>();;

	//bi-directional many-to-one association to GxAccountType
	@ManyToOne
	@JoinColumn(name = "oid_account_type")
	private GxAccountType gxAccountType;

	//bi-directional many-to-one association to GxTransaction
	@OneToMany(mappedBy = "gxAccount", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GxTransaction> gxTransactions = new ArrayList<GxTransaction>();

	@OneToMany(mappedBy = "gxAccount", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GxAccountBalance> gxAccountBalances = new ArrayList<>();

	public GxAccount() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public GxAccount getGxParentAccount() {
		return this.gxParentAccount;
	}

	public void setGxParentAccount(GxAccount gxParentAccount) {
		this.gxParentAccount = gxParentAccount;
	}

	public List<GxAccount> getGxChildAccounts() {
		return this.gxChildAccounts;
	}

	public void setGxChildAccounts(List<GxAccount> gxChildAccounts) {
		this.gxChildAccounts = gxChildAccounts;
	}

	public GxAccount addGxChildAccount(GxAccount gxChildAccount) {
		getGxChildAccounts().add(gxChildAccount);
		gxChildAccount.setGxParentAccount(this);

		return gxChildAccount;
	}

	public GxAccount removeGxChildAccount(GxAccount gxChildAccount) {
		getGxChildAccounts().remove(gxChildAccount);
		gxChildAccount.setGxParentAccount(null);

		return gxChildAccount;
	}

	public GxAccountType getGxAccountType() {
		return this.gxAccountType;
	}

	public void setGxAccountType(GxAccountType gxAccountType) {
		this.gxAccountType = gxAccountType;
	}

	public List<GxTransaction> getGxTransactions() {
		return this.gxTransactions;
	}

	public void setGxTransactions(List<GxTransaction> gxTransactions) {
		this.gxTransactions = gxTransactions;
	}

	public GxTransaction addGxTransaction(GxTransaction gxTransaction) {
		getGxTransactions().add(gxTransaction);
		gxTransaction.setGxAccount(this);

		return gxTransaction;
	}

	public GxTransaction removeGxTransaction(GxTransaction gxTransaction) {
		getGxTransactions().remove(gxTransaction);
		gxTransaction.setGxAccount(null);

		return gxTransaction;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public List<GxAccountBalance> getGxAccountBalances() {
		return gxAccountBalances;
	}

	public void setGxAccountBalances(List<GxAccountBalance> gxAccountBalances) {
		this.gxAccountBalances = gxAccountBalances;
	}

	public GxAccountBalance addGxAccountBalance(GxAccountBalance gxAccountBalance) {
		getGxAccountBalances().add(gxAccountBalance);
		gxAccountBalance.setGxAccount(this);

		return gxAccountBalance;
	}

	public GxAccountBalance removeGxAccountBalance(GxAccountBalance gxAccountBalance) {
		getGxAccountBalances().remove(gxAccountBalance);
		gxAccountBalance.setGxAccount(null);

		return gxAccountBalance;
	}

}