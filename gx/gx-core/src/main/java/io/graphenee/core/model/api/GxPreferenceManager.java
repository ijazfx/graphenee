package io.graphenee.core.model.api;

import org.json.JSONObject;

import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.entity.GxNamespace;

public interface GxPreferenceManager {

	JSONObject loadUserPreference(GxAuthenticatedUser user);

	void saveUserPreference(GxAuthenticatedUser user, JSONObject preference);

	JSONObject loadNamespacePreference(GxNamespace namespace);

	void saveNamespacePreference(GxNamespace namespace, JSONObject preference);
}
