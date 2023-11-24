package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "gx_password_history")
@NamedQuery(name = "GxPasswordHistory.findAll", query = "SELECT g FROM GxPasswordHistory g")
public class GxPasswordHistory extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;
	@ManyToOne
	@JoinColumn(name = "oid_user_account")
	private GxUserAccount gxUserAccount;
	@Column(name = "hashed_password")
	private String hashedPassword;
	@Column(name = "password_date")
	private Timestamp passwordDate;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public GxUserAccount getGxUserAccount() {
		return gxUserAccount;
	}

	public void setGxUserAccount(GxUserAccount gxUserAccount) {
		this.gxUserAccount = gxUserAccount;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public Timestamp getPasswordDate() {
		return passwordDate;
	}

	public void setPasswordDate(Timestamp passwordDate) {
		this.passwordDate = passwordDate;
	}

}
