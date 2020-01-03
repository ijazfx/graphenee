package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gx_balance_sheet_view")
public class GxBalanceSheet extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "oid")
	private Integer oid;

	private Timestamp month;

	@Column(name = "account_name")
	private String accountName;

	@Column(name = "oid_account")
	private Integer oidAccount;

	@Column(name = "oid_parent_account")
	private Integer oidParentAccount;

	@Column(name = "parent_account_name")
	private String parentAccountName;

	@Column(name = "oid_account_type")
	private Integer oidAccountType;

	@Column(name = "account_type_name")
	private String accountTypeName;

	@Column(name = "account_type_code")
	private String accountTypeCode;

	private Double amount;

	@Column(name = "oid_namespace")
	private Integer oidNamespace;

	public Integer getOid() {
		return oid;
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

	public Integer getOidParentAccount() {
		return oidParentAccount;
	}

	public void setOidParentAccount(Integer oidParentAccount) {
		this.oidParentAccount = oidParentAccount;
	}

	public String getParentAccountName() {
		return parentAccountName;
	}

	public void setParentAccountName(String parentAccountName) {
		this.parentAccountName = parentAccountName;
	}

	public Integer getOidAccountType() {
		return oidAccountType;
	}

	public void setOidAccountType(Integer oidAccountType) {
		this.oidAccountType = oidAccountType;
	}

	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
	}

	public String getAccountTypeCode() {
		return accountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

}
