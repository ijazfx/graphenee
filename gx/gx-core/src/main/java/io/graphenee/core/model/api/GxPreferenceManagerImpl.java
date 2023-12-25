package io.graphenee.core.model.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxDashboardUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;

@Service
public class GxPreferenceManagerImpl implements GxPreferenceManager {

	@Autowired
	GxDataService service;

	@Override
	public JSONObject loadUserPreference(GxAuthenticatedUser user) {
		if (user instanceof GxDashboardUser) {
			GxUserAccount userAccount = ((GxDashboardUser) user).getUser();
			GxUserAccount savedUser = service.findUserAccount(userAccount.getOid());
			String preferences = savedUser.getPreferences();
			try {
				return new JSONObject(preferences);
			} catch (Exception ex) {
				return new JSONObject();
			}
		}
		return new JSONObject();
	}

	@Override
	public void saveUserPreference(GxAuthenticatedUser user, JSONObject preference) {
		if (user instanceof GxDashboardUser) {
			GxUserAccount userAccount = ((GxDashboardUser) user).getUser();
			userAccount.setPreferences(preference.toString());
			GxUserAccount savedUser = service.findUserAccount(userAccount.getOid());
			savedUser.setPreferences(preference.toString());
			service.save(savedUser);
		}
	}

	@Override
	public JSONObject loadNamespacePreference(GxNamespace namespace) {
		throw new UnsupportedOperationException("To be implemented in future");
	}

	@Override
	public void saveNamespacePreference(GxNamespace namespace, JSONObject preference) {
		throw new UnsupportedOperationException("To be implemented in future");
	}

}
