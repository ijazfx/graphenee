package io.graphenee.core.model.bean;

import java.io.Serializable;

import io.graphenee.core.model.BeanFault;

public class GxPasswordPolicyBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer oid;
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
	private BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
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

	public BeanFault<Integer, GxNamespaceBean> getGxNamespaceBeanFault() {
		return gxNamespaceBeanFault;
	}

	public void setGxNamespaceBeanFault(BeanFault<Integer, GxNamespaceBean> gxNamespaceBeanFault) {
		this.gxNamespaceBeanFault = gxNamespaceBeanFault;
	}

	public String getPasswordPolicyName() {
		return passwordPolicyName;
	}

	public void setPasswordPolicyName(String passwordPolicyName) {
		this.passwordPolicyName = passwordPolicyName;
	}
}
