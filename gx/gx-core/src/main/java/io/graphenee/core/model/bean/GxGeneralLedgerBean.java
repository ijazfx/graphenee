package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.util.TRCalendarUtil;

public class GxGeneralLedgerBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Timestamp transactionDate;
	private String accountName;
	private Integer oidAccount;
	private Integer oidAccountType;
	private String accountTypeName;
	private String description;
	private Double amount;
	private Double balance;
	private Integer oidVoucher;
	private String formattedDate;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDebit() {
		return getAmount() > 0 ? getAmount() : 0;
	}

	public Double getCredit() {
		return getAmount() < 0 ? Math.abs(getAmount()) : 0;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Timestamp getTransactionDateTime() {
		return transactionDate;
	}

	public Integer getOidVoucher() {
		return oidVoucher;
	}

	public void setOidVoucher(Integer oidVoucher) {
		this.oidVoucher = oidVoucher;
	}

	public String getFormattedDate() {
		return TRCalendarUtil.dateFormatter.format(transactionDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oid == null) ? 0 : oid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GxGeneralLedgerBean other = (GxGeneralLedgerBean) obj;
		if (oid == null) {
			if (other.oid != null)
				return false;
		} else if (!oid.equals(other.oid))
			return false;
		return true;
	}

}
