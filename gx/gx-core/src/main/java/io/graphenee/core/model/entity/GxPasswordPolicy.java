package io.graphenee.core.model.entity;

import java.io.Serializable;

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
@Table(name = "gx_password_policy")
@NamedQuery(name = "GxPasswordPolicy.findAll", query = "SELECT g FROM GxPasswordPolicy g")
public class GxPasswordPolicy extends io.graphenee.core.model.GxMappedSuperclass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;

	@ManyToOne
	@JoinColumn(name = "oid_namespace")
	private GxNamespace gxNamespace;
	@Column(name = "max_history")
	private Integer maxHistory;
	@Column(name = "max_age")
	private Integer maxAge;
	@Column(name = "min_length")
	private Integer minLength;
	@Column(name = "is_user_username_allowed")
	private Boolean isUserUsernameAllowed;
	@Column(name = "max_allowed_matching_user_name")
	private Integer maxAllowedMatchingUserName;
	@Column(name = "min_uppercase")
	private Integer minUppercase;
	@Column(name = "min_lowercase")
	private Integer minLowercase;
	@Column(name = "min_numbers")
	private Integer minNumbers;
	@Column(name = "min_special_charaters")
	private Integer minSpecialCharacters;
	@Column(name = "is_active")
	private Boolean isActive;
	@Column(name = "password_policy_name")
	private String passwordPolicyName;

	public GxPasswordPolicy() {

	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public GxNamespace getGxNamespace() {
		return gxNamespace;
	}

	public void setGxNamespace(GxNamespace gxNamespace) {
		this.gxNamespace = gxNamespace;
	}

	public Integer getMaxHistory() {
		return maxHistory;
	}

	public void setMaxHistory(Integer maxHistory) {
		this.maxHistory = maxHistory;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Boolean getIsUserUsernameAllowed() {
		return isUserUsernameAllowed;
	}

	public void setIsUserUsernameAllowed(Boolean isUserUsernameAllowed) {
		this.isUserUsernameAllowed = isUserUsernameAllowed;
	}

	public Integer getMaxAllowedMatchingUserName() {
		return maxAllowedMatchingUserName;
	}

	public void setMaxAllowedMatchingUserName(Integer maxAllowedMatchingUserName) {
		this.maxAllowedMatchingUserName = maxAllowedMatchingUserName;
	}

	public Integer getMinUppercase() {
		return minUppercase;
	}

	public void setMinUppercase(Integer minUppercase) {
		this.minUppercase = minUppercase;
	}

	public Integer getMinLowercase() {
		return minLowercase;
	}

	public void setMinLowercase(Integer minLowercase) {
		this.minLowercase = minLowercase;
	}

	public Integer getMinNumbers() {
		return minNumbers;
	}

	public void setMinNumbers(Integer minNumbers) {
		this.minNumbers = minNumbers;
	}

	public Integer getMinSpecialCharacters() {
		return minSpecialCharacters;
	}

	public void setMinSpecialCharacters(Integer minSpecialCharacters) {
		this.minSpecialCharacters = minSpecialCharacters;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPasswordPolicyName() {
		return passwordPolicyName;
	}

	public void setPasswordPolicyName(String passwordPolicyName) {
		this.passwordPolicyName = passwordPolicyName;
	}

}
