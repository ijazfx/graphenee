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
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getUsername() {
		return username;
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
	public String getMobileNumber() {
		return null;
	}

	@Override
	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap) {
		return true;
	}

	@Override
	public boolean canDoAction(String resource, String action, Map<String, Object> keyValueMap, boolean forceRefresh) {
		return true;
	}

	@Override
	public String getName() {
		return getUser();
	}

}
