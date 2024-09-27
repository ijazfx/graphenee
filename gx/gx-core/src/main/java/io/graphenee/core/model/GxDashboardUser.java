package io.graphenee.core.model;

import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import io.graphenee.core.model.bean.GxNamespaceBean;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.util.enums.GenderEnum;

public class GxDashboardUser extends AbstractDashboardUser<GxUserAccountBean> {

	private AtomicInteger notificationCount = new AtomicInteger();

	public GxDashboardUser(GxUserAccountBean user) {
		super(user);
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
		return getUser().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getUser().setPassword(password);
	}

	@Override
	public boolean isPasswordChangeRequired() {
		return getUser().getIsPasswordChangeRequired();
	}

	@Override
	public GenderEnum getGender() {
		return getUser().getGender();
	}

	@Override
	public void setGender(GenderEnum gender) {
		getUser().setGender(gender);
	}

	@Override
	public boolean canDoAction(String resource, String action) {
		return getUser().canDoAction(resource, action);
	}

	@Override
	public boolean canDoAction(String resource, String action, boolean forceRefresh) {
		return getUser().canDoAction(resource, action, forceRefresh);
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

	public byte[] getProfilePhoto() {
		return getUser().getProfileImage();
	}

	@Override
	public int getUnreadNotificationCount() {
		return notificationCount.get();
	}

	@Override
	public void setUnreadNotificationCount(int count) {
		notificationCount.set(count);
	}

	public <T> T getPreference(String key) {
		return getUser().getPreference(key);
	}

	public <T> void setPreference(String key, T value) {
		getUser().setPreference(key, value);
	}

	public void clearPreference(String key) {
		getUser().clearPreference(key);
	}

	public void createAllPreferences() {
		getUser().createAllPreferences();
	}

	@Override
	public BeanFault<Integer, GxNamespaceBean> getNamespaceFault() {
		return getUser().getNamespaceFault();
	}

	@Override
	public void setNamespaceFault(BeanFault<Integer, GxNamespaceBean> namespaceFault) {
		getUser().setNamespaceFault(namespaceFault);
	}

	@Override
	public Integer getOid() {
		return getUser().getOid();
	}

	@Override
	public void setOid(Integer oid) {
		getUser().setOid(oid);
	}

}
