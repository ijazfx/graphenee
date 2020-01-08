package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class GxIncomeStatementBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Timestamp month;
	private String accountName;
	private Integer oidAccount;
	private Integer oidAccountType;
	private String accountTypeName;
	private String accountTypeCode;
	private Double amount;
	private Integer oidNamespace;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getMonth() {
		return month;
	}

	public void setMonth(Timestamp month) {
		this.month = month;
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

}
