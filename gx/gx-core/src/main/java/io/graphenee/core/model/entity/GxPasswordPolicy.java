package io.graphenee.core.model.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gx_password_policy")
public class GxPasswordPolicy extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer maxHistory = 3;
	private Integer maxAge = 60;
	private Integer minLength = 8;
	private Boolean isUserUsernameAllowed = false;
	private Integer maxAllowedMatchingUserName = 2;
	private Integer minUppercase = 1;
	private Integer minLowercase = 1;
	private Integer minNumbers = 1;
	private Integer minSpecialCharacters = 1;
	private Boolean isActive = false;
	private String passwordPolicyName;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace namespace;

}
