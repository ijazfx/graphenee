package io.graphenee.vaadin.flow.security;

import java.util.concurrent.atomic.AtomicInteger;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.util.enums.GenderEnum;

public class GxUserAccountAuthenticatedUser implements GxAuthenticatedUser {

	private GxUserAccountBean user;
	private AtomicInteger unreadNotificationCount = new AtomicInteger(0);

	public GxUserAccountAuthenticatedUser(GxUserAccountBean user) {
		this.user = user;
	}

	@Override
	public int getUnreadNotificationCount() {
		return unreadNotificationCount.get();
	}

	@Override
	public void setUnreadNotificationCount(int count) {
		unreadNotificationCount.set(count);
	}

	@Override
	public byte[] getProfilePhoto() {
		return user.getProfileImage();
	}

	@Override
	public String getFirstName() {
		return user.getFirstName();
	}

	@Override
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	@Override
	public String getLastName() {
		return user.getLastName();
	}

	@Override
	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public void setUsername(String username) {
		this.setUsername(username);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public void setPassword(String password) {
		user.setPassword(password);
	}

	@Override
	public GenderEnum getGender() {
		return user.getGender();
	}

	@Override
	public void setGender(GenderEnum gender) {
		user.setGender(gender);
	}

	@Override
	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public void setEmail(String email) {
		user.setEmail(email);
	}

	@Override
	public String getMobileNumber() {
		return null;
	}

	@Override
	public void setMobileNumber(String mobileNumber) {
	}

}
