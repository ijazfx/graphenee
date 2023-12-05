package io.graphenee.core.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "gx_password_history")
public class GxPasswordHistory extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {

	private static final long serialVersionUID = 1L;

	private String hashedPassword;
	private Timestamp passwordDate;

	@ManyToOne
	@JoinColumn(name = "oid_user_account")
	private GxUserAccount userAccount;

}
