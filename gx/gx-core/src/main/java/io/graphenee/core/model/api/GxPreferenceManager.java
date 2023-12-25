package io.graphenee.core.model.api;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxNamespace;

public interface GxPreferenceManager {

	String loadUserPreference(GxAuthenticatedUser user);

	void saveUserPreference(GxAuthenticatedUser user, String json);

	String loadNamespacePreference(GxNamespace namespace);

	void saveNamespacePreference(GxNamespace namespace, String json);
}
