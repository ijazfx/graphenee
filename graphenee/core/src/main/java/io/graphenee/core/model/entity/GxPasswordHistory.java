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
