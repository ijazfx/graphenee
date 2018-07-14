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
package io.graphenee.vaadin.domain;

import io.graphenee.core.model.bean.GxUserAccountBean;

public class GxDashboardUser extends AbstractDashboardUser<GxUserAccountBean> {

	public GxDashboardUser(GxUserAccountBean user) {
		super(user);
	}

	@Override
	public String getFirstName() {
		return getUser().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
	}

	@Override
	public String getLastName() {
		return getUser().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
	}

	@Override
	public String getUsername() {
		return getUser().getUsername();
	}

	@Override
	public void setUsername(String username) {
	}

	@Override
	public String getPassword() {
		return getUser().getPassword();
	}

	@Override
	public void setPassword(String password) {
	}

	@Override
	public GenderEnum getGender() {
		if (getUser().getGender() == null)
			return GenderEnum.Undisclosed;
		if (getUser().getGender().getGenderCode().equals(GenderEnum.Male.getGenderCode()))
			return GenderEnum.Male;
		if (getUser().getGender().getGenderCode().equals(GenderEnum.Female.getGenderCode()))
			return GenderEnum.Female;
		return GenderEnum.Undisclosed;
	}

	@Override
	public void setGender(GenderEnum gender) {
	}

	@Override
	public boolean canDoAction(String resource, String action) {
		return getUser().canDoAction(resource, action);
	}

	@Override
	public boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return getUser().canDoAction(resource, action, forceRefresh);
	}

}
