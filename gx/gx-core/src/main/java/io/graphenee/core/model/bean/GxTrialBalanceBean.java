package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class GxTrialBalanceBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String accountName;
	private Integer oidAccount;
	private String accountTypeName;
	private Integer oidAccountType;
	private Double debit;
	private Double credit;
	private Timestamp month;

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

	public Double getDebit() {
		return Math.abs(debit);
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Timestamp getMonth() {
		return month;
	}

	public void setMonth(Timestamp month) {
		this.month = month;
	}

}
