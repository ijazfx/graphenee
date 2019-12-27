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
 * The persistent class for the gx_account_balance database table.
 * 
 */
@Entity
@Table(name = "gx_account_balance")
@NamedQuery(name = "GxAccountBalance.findAll", query = "SELECT g FROM GxAccountBalance g")
public class GxAccountBalance extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@ManyToOne
	@JoinColumn(name = "oid_account")
	private GxAccount gxAccount;

	@Column(name = "opening_balance")
	private Double openingBalance;

	@Column(name = "opening_year")
	private Timestamp openingYear;

	public GxAccountBalance() {
	}

	public Integer getOid() {
		return this.oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public double getOpeningBalance() {
		return this.openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Timestamp getOpeningYear() {
		return this.openingYear;
	}

	public void setOpeningYear(Timestamp openingYear) {
		this.openingYear = openingYear;
	}

	public GxAccount getGxAccount() {
		return gxAccount;
	}

	public void setGxAccount(GxAccount gxAccount) {
		this.gxAccount = gxAccount;
	}

}