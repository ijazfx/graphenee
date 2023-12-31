package io.graphenee.core.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.graphenee.core.GxDataService;
import io.graphenee.core.GxPreferenceManager;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.GxDashboardUser;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxUserAccount;

@Service
public class GxPreferenceManagerImpl implements GxPreferenceManager {

	@Autowired
	GxDataService service;

	@Override
	public String loadUserPreference(GxAuthenticatedUser user) {
		if (user instanceof GxDashboardUser) {
			GxUserAccount userAccount = ((GxDashboardUser) user).getUser();
			GxUserAccount savedUser = service.findUserAccount(userAccount.getOid());
			return savedUser.getPreferences();
		}
		return "{}";
	}

	@Override
	public void saveUserPreference(GxAuthenticatedUser user, String json) {
		if (user instanceof GxDashboardUser) {
			GxUserAccount userAccount = ((GxDashboardUser) user).getUser();
			userAccount.setPreferences(json);
			GxUserAccount savedUser = service.findUserAccount(userAccount.getOid());
			savedUser.setPreferences(json);
			service.save(savedUser);
		}
	}

	@Override
	public String loadNamespacePreference(GxNamespace namespace) {
		throw new UnsupportedOperationException("To be implemented in future");
	}

	@Override
	public void saveNamespacePreference(GxNamespace namespace, String json) {
		throw new UnsupportedOperationException("To be implemented in future");
	}

}
