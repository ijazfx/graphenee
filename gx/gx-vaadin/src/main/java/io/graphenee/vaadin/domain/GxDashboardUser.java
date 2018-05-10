package io.graphenee.vaadin.domain;

import com.graphenee.core.model.bean.GxUserAccountBean;

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

}
