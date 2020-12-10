/*******************************************************************************
 * Copyright (c) 2016, 2018 Farrukh Ijaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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

	@Override
	public String toString() {
		return getFullName();
	}

}
