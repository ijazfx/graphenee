package io.graphenee.core.model;

import java.util.HashMap;
import java.util.Map;

public class GxAbstractCredentials {

	private Map<String, String> credentials = new HashMap<>();

	public String getCredential(String key) {
		return credentials.get(key);
	}

	protected void setCredential(String key, String credential) {
		credentials.put(key, credential);
	}

}
