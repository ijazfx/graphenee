package io.graphenee.jbpm.embedded;

import io.graphenee.core.model.bean.GxUserAccountBean;

public class GxUserAccountAssigneeWrapper implements GxAssignee {

	public GxUserAccountBean userAccount;

	public GxUserAccountAssigneeWrapper(GxUserAccountBean userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public String getUsername() {
		return userAccount.getUsername();
	}

	@Override
	public String getFullName() {
		return userAccount.getFullName();
	}

	@Override
	public String getEmail() {
		return userAccount.getEmail();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userAccount == null) ? 0 : userAccount.hashCode());
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
		GxUserAccountAssigneeWrapper other = (GxUserAccountAssigneeWrapper) obj;
		if (userAccount == null) {
			if (other.userAccount != null)
				return false;
		} else if (!userAccount.equals(other.userAccount))
			return false;
		return true;
	}

}
