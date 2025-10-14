package io.graphenee.core.model.entity;

import java.time.LocalDate;

import io.graphenee.core.model.GxMappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_password_history")
public class GxPasswordHistory extends GxMappedSuperclass {

	private String hashedPassword;
	private LocalDate passwordDate;

	@ManyToOne
	@JoinColumn(name = "oid_user_account")
	private GxUserAccount userAccount;

}
