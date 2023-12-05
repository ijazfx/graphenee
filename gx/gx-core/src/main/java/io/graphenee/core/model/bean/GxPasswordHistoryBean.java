package io.graphenee.core.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import io.graphenee.core.model.BeanFault;

public class GxPasswordHistoryBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer oid;
	private String hashedPassword;
	private Timestamp passwordDate;
	private BeanFault<Integer, GxUserAccountBean> userAccountBeanFault;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
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

	public BeanFault<Integer, GxUserAccountBean> getUserAccountBeanFault() {
		return userAccountBeanFault;
	}

	public void setUserAccountBeanFault(BeanFault<Integer, GxUserAccountBean> userAccountBeanFault) {
		this.userAccountBeanFault = userAccountBeanFault;
	}

}
