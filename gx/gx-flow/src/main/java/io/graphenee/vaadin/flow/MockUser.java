package io.graphenee.vaadin.flow;

import java.util.Map;

import io.graphenee.util.enums.GenderEnum;

public final class MockUser extends AbstractDashboardUser<String> {

	String firstName;
	String lastName;
	String username;
	String password;
	GenderEnum gender;

	public MockUser() {
		super("Mock User");
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public GenderEnum getGender() {
		return this.gender;
	}

	@Override
	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	@Override
	public Integer getOid() {
		return 0;
	}

	@Override
	public byte[] getProfilePhoto() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
	}

	@Override
	public void setEmail(String email) {
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
		return true;
	}

	@Override
	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh) {
		return true;
	}

}
