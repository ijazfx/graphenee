package io.graphenee.core.flow;

import java.util.Map;

import io.graphenee.core.model.entity.GxGender;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.util.enums.GenderEnum;
import io.graphenee.vaadin.flow.AbstractDashboardUser;

public class GxUserAccountDashboardUser extends AbstractDashboardUser<GxUserAccount> {

	public GxUserAccountDashboardUser(GxUserAccount user) {
		super(user);
	}

	@Override
	public Integer getOid() {
		return getUser().getOid();
	}

	@Override
	public void setProfilePhoto(byte[] picture) {
		getUser().setProfileImage(picture);
	}

	@Override
	public byte[] getProfilePhoto() {
		return getUser().getProfileImage();
	}

	@Override
	public String getFirstName() {
		return getUser().getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		getUser().setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return getUser().getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		getUser().setLastName(lastName);
	}

	@Override
	public String getUsername() {
		return getUser().getUsername();
	}

	@Override
	public void setUsername(String username) {
		getUser().setUsername(username);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public void setPassword(String password) {
	}

	@Override
	public GenderEnum getGender() {
		GxGender gender = getUser().getGender();
		if (gender == null)
			return GenderEnum.Undisclosed;
		if (gender.getGenderCode().equalsIgnoreCase("M"))
			return GenderEnum.Male;
		if (gender.getGenderCode().equalsIgnoreCase("F"))
			return GenderEnum.Female;
		return GenderEnum.Undisclosed;
	}

	@Override
	public void setGender(GenderEnum gender) {
	}

	@Override
	public String getEmail() {
		return getUser().getEmail();
	}

	@Override
	public void setEmail(String email) {
		getUser().setEmail(email);
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public void setMobileNumber(String mobileNumber) {
	}

	@Override
	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap) {
		return getUser().canDoAction(resource, action, keyValueMap);
	}

	@Override
	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh) {
		return getUser().canDoAction(resource, action, keyValueMap, forceRefresh);
	}

}
