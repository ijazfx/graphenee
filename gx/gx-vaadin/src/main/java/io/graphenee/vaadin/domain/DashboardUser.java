/*******************************************************************************
 * Copyright (c) 2016, 2017, Graphenee
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

import com.graphenee.gx.theme.graphenee.GrapheneeTheme;
import com.vaadin.server.Resource;

public interface DashboardUser {

	enum GenderEnum {
		Male("M"), Female("F"), Undisclosed("X");

		private String genderCode;

		private GenderEnum(String genderCode) {
			this.genderCode = genderCode;
		}

		public String getGenderCode() {
			return genderCode;
		}

	}

	default Resource getProfilePhoto() {
		if (getGender() != null && getGender() == GenderEnum.Female) {
			return GrapheneeTheme.AVATAR_FEMALE;
		}
		return GrapheneeTheme.AVATAR_MALE;
	}

	default void setProfilePhoto(Resource resource) {
	}

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getUsername();

	void setUsername(String username);

	String getPassword();

	void setPassword(String password);

	GenderEnum getGender();

	void setGender(GenderEnum gender);

	default String getFirstNameLastName() {
		if (getFirstName() != null) {
			if (getLastName() != null) {
				return getFirstName() + " " + getLastName();
			}
			return getFirstName();
		}
		if (getLastName() != null) {
			return getLastName();
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	default String getLastNameFirstName() {
		if (getLastName() != null) {
			if (getFirstName() != null) {
				return getLastName() + ", " + getFirstName();
			}
			return getLastName();
		}
		if (getFirstName() != null) {
			return getFirstName();
		}
		if (getUsername() != null) {
			return getUsername();
		}
		return "Anonymous";
	}

	default boolean canDoAction(String resource, String action) {
		return true;
	}

	default boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return true;
	}

}