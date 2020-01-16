package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gx_trial_balance_view")
public class GxTrialBalance extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "oid")
	private Integer oid;

	@Column(name = "account_code")
	private Integer accountCode;

	@Column(name = "account_name")
	private String accountName;

	@Column(name = "oid_account")
	private Integer oidAccount;

	@Column(name = "account_type_name")
	private String accountTypeName;

	@Column(name = "oid_account_type")
	private Integer oidAccountType;

	private Double amount;

	@Column(name = "oid_namespace")
	private Integer oidNamespace;

	private Timestamp month;

	public Integer getOid() {
		return oid;
	}

	public Integer getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(Integer accountCode) {
		this.accountCode = accountCode;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Integer getOidAccount() {
		return oidAccount;
	}

	public void setOidAccount(Integer oidAccount) {
		this.oidAccount = oidAccount;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public Integer getOidAccountType() {
		return oidAccountType;
	}

	public void setOidAccountType(Integer oidAccountType) {
		this.oidAccountType = oidAccountType;
	}

	public Integer getOidNamespace() {
		return oidNamespace;
	}

	public void setOidNamespace(Integer oidNamespace) {
		this.oidNamespace = oidNamespace;
	}

	public Timestamp getMonth() {
		return month;
	}

	public void setMonth(Timestamp month) {
		this.month = month;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
